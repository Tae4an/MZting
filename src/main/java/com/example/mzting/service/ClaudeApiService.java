package com.example.mzting.service;

import com.example.mzting.config.ClaudeConfig;
import com.example.mzting.dto.ClaudeResponse;
import com.example.mzting.entity.Profile;
import com.example.mzting.exception.ClaudeApiJsonParsingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.mzting.exception.ClaudeApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;

/**
 * ClaudeApiService 클래스
 * Claude AI API와의 통신을 담당하는 서비스 클래스.
 * 특정 MBTI 유형에 따른 프로필을 사용하여 Claude AI와의 대화를 생성하고 응답을 관리.
 */
@Service
public class ClaudeApiService {
    private static final Logger logger = LoggerFactory.getLogger(ClaudeApiService.class);

    private final ClaudeConfig claudeConfig; // Claude API 설정
    private final RestTemplate restTemplate; // RestTemplate 객체
    private final ObjectMapper objectMapper; // JSON 파싱을 위한 ObjectMapper
    private final ProfileService profileService; // 프로필 관리를 위한 ProfileService


    public ClaudeApiService(ClaudeConfig claudeConfig, RestTemplate restTemplate,
                            ObjectMapper objectMapper, ProfileService profileService) {
        this.claudeConfig = claudeConfig;
        this.restTemplate = restTemplate;
        this.profileService = profileService;

        this.objectMapper = objectMapper;
        this.objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        this.objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
    }

    @Cacheable(value = "claudeResponses", key = "#mbti + '-' + #messages.hashCode() + '-' + #context.hashCode()")
    public ClaudeResponse getClaudeResponseByMbti(String mbti, List<Map<String, String>> messages, Map<String, String> context) {
        logger.info("Generating Claude response for MBTI: {}", mbti);
        Profile profile = profileService.getRandomProfileByMbti(mbti)
                .orElseThrow(() -> new RuntimeException("No profile found for MBTI: " + mbti));

        String fixedPrompt = claudeConfig.getFixedPrompt();
        String prompt = fixedPrompt + "\n" + profileService.generatePrompt(profile) + "\n컨텍스트: " + context;
        logger.debug("Generated prompt: {}", maskSensitiveInfo(prompt));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", claudeConfig.getApiKey());
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-3-5-sonnet-20240620");
        requestBody.put("max_tokens", 1024);
        requestBody.put("system", prompt);
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        logger.debug("Sending request to Claude API");
        ResponseEntity<String> response = restTemplate.exchange(
                claudeConfig.getApiUrl(),
                HttpMethod.POST,
                entity,
                String.class
        );

        logger.info("Received response from Claude API. Status code: {}", response.getStatusCode());
        logger.debug("Claude API raw response: {}", maskSensitiveInfo(response.getBody()));

        return parseClaudeResponse(response.getBody());
    }

    private ClaudeResponse parseClaudeResponse(String responseBody) {
        try {
            logger.debug("Parsing Claude API response");
            JsonNode rootNode = objectMapper.readTree(responseBody);

            if (rootNode.path("content").isEmpty()) {
                logger.warn("Content array is empty in the response");
                throw new ClaudeApiException("Empty content in API response");
            }

            String jsonText = rootNode.path("content").get(0).path("text").asText();
            logger.debug("Extracted text from response: {}", maskSensitiveInfo(jsonText));

            JsonNode responseJson = objectMapper.readTree(sanitizeJsonString(jsonText));

            ClaudeResponse claudeResponse = new ClaudeResponse();
            claudeResponse.setText(getNodeValueSafely(responseJson, "text", String.class));
            claudeResponse.setFeel(getNodeValueSafely(responseJson, "feel", String.class));
            claudeResponse.setEvaluation(getNodeValueSafely(responseJson, "evaluation", String.class));
            claudeResponse.setScore(getNodeValueSafely(responseJson, "score", Integer.class));
            claudeResponse.setStage1Complete(getNodeValueSafely(responseJson, "stage1_complete", Boolean.class));
            claudeResponse.setStage2Complete(getNodeValueSafely(responseJson, "stage2_complete", Boolean.class));
            claudeResponse.setStage3Complete(getNodeValueSafely(responseJson, "stage3_complete", Boolean.class));

            logger.info("Successfully parsed Claude API response: {}", maskSensitiveInfo(claudeResponse.toString()));
            return claudeResponse;
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error while parsing Claude API response", e);
            throw new ClaudeApiJsonParsingException("Failed to parse Claude API JSON response", e);
        } catch (Exception e) {
            logger.error("Unexpected error while parsing Claude API response", e);
            throw new ClaudeApiException("Failed to parse Claude API response", e);
        }
    }

    private String sanitizeJsonString(String jsonString) {
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            return jsonString;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(jsonString.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r"));
        sb.append("}");
        return sb.toString();
    }

    private <T> T getNodeValueSafely(JsonNode node, String fieldName, Class<T> clazz) {
        JsonNode field = node.get(fieldName);
        if (field == null) {
            logger.warn("Field '{}' is missing", fieldName);
            return null;
        }
        try {
            if (clazz == String.class) {
                return clazz.cast(field.asText());
            } else if (clazz == Integer.class) {
                return clazz.cast(field.asInt());
            } else if (clazz == Boolean.class) {
                return clazz.cast(field.asBoolean());
            }
        } catch (Exception e) {
            logger.warn("Error parsing field '{}': {}", fieldName, e.getMessage());
        }
        return null;
    }

    private String maskSensitiveInfo(String input) {
        return input.replaceAll("(\\w+@\\w+\\.\\w+)", "***@***.***")
                .replaceAll("\\d{10,}", "**********");
    }
}
