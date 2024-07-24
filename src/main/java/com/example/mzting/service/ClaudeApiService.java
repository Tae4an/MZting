package com.example.mzting.service;

import com.example.mzting.config.ClaudeConfig;
import com.example.mzting.dto.ClaudeResponse;
import com.example.mzting.entity.Profile;
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

@Service
public class ClaudeApiService {
    private static final Logger logger = LoggerFactory.getLogger(ClaudeApiService.class);

    private final ClaudeConfig claudeConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ProfileService profileService;

    private static final String FIXED_PROMPT = "너는 소개팅에 참여한 사람이야. 대화를 이어나가되, 상대방이 당신이 AI라는 것을 알아채지 못하도록 자연스럽게 대화해. 소개팅이라는 공식적인 자리임을 감안하여 존댓말을 사용해주세요. 그리고 말투는 합니다 보다는 해요 형식으로 하고, 답장을 할 때마다 질문을 무조건 하려고 하지 말고, 문장의 맥락에 따라서 질문을 할 때도 있고, 하지 않을 때를 판단해서 질문해. 질문할 때는 한 번에 하나의 질문만 해.답변은 반드시 다음 JSON 형식으로 작성해: {\\\"text\\\": \\\"질문에 대한 답변 내용\\\", \\\"feel\\\": \\\"질문에 대한 느낌\\\", \\\"evaluation\\\": \\\"상대의 질문이 어떻게 다가오는지에 대한 평가\\\", \\\"score\\\": 0부터 10 사이의 숫자}\";";


    public ClaudeApiService(ClaudeConfig claudeConfig, RestTemplate restTemplate,
                            ObjectMapper objectMapper, ProfileService profileService) {
        this.claudeConfig = claudeConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.profileService = profileService;
    }

    @Cacheable(value = "claudeResponses", key = "#mbti + '-' + #messages.hashCode()")
    public ClaudeResponse getClaudeResponseByMbti(String mbti, List<Map<String, String>> messages) {
        Profile profile = profileService.getRandomProfileByMbti(mbti)
                .orElseThrow(() -> new RuntimeException("No profile found for MBTI: " + mbti));

        String prompt = FIXED_PROMPT + "\n" + profileService.generatePrompt(profile);

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

        ResponseEntity<String> response = restTemplate.exchange(
                claudeConfig.getApiUrl(),
                HttpMethod.POST,
                entity,
                String.class
        );

        logger.info("Claude API raw response: " + response.getBody());

        return parseClaudeResponse(response.getBody());
    }


    private ClaudeResponse parseClaudeResponse(String responseBody) {
        try {
            logger.debug("Parsing Claude API response");

            // 전체 응답을 JSON으로 파싱
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // 'content' 배열의 첫 번째 요소에서 'text' 필드를 가져옴
            String jsonText = rootNode.path("content").get(0).path("text").asText();

            // jsonText를 파싱하여 ClaudeResponse 객체 생성
            JsonNode responseJson = objectMapper.readTree(jsonText);

            ClaudeResponse claudeResponse = new ClaudeResponse();
            claudeResponse.setText(getTextSafely(responseJson, "text"));
            claudeResponse.setFeel(getTextSafely(responseJson, "feel"));
            claudeResponse.setEvaluation(getTextSafely(responseJson, "evaluation"));
            claudeResponse.setScore(getIntSafely(responseJson, "score"));

            logger.info("Successfully parsed Claude API response");
            return claudeResponse;
        } catch (Exception e) {
            logger.error("Error parsing Claude API response", e);
            logger.error("Raw response: " + responseBody);
            throw new ClaudeApiException("Failed to parse Claude API response", e);
        }
    }

    private String getTextSafely(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field != null && field.isTextual()) {
            return field.asText();
        } else {
            logger.warn("Field '{}' is missing or not a text", fieldName);
            return "";
        }
    }

    private int getIntSafely(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field != null && field.isInt()) {
            return field.asInt();
        } else {
            logger.warn("Field '{}' is missing or not an integer", fieldName);
            return 0;
        }
    }
}