package com.example.mzting.service;

import com.example.mzting.config.ClaudeConfig;
import com.example.mzting.dto.ClaudeResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.mzting.exception.ClaudeApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;

@Service
public class ClaudeApiService {
    private static final Logger logger = LoggerFactory.getLogger(ClaudeApiService.class);

    private final ClaudeConfig claudeConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String INITIAL_PROMPT = "너는 소개팅에 나온 23살 여성이고 이름은 \"이지은\" MBTI는 INFJ, 취미는 게임, 키는 165, 직업은 작가야 대화를 줄테니 상대방이 너가 AI라는 것을 알아채지 못 하도록 사람처럼 자연스럽게 행동해, 소개팅이라는 공식적인 자리인 것을 감안하여 해요체를 위주로 사용하고, 다음과 같은 INFJ성향을 반영해서 답변해줘 \"생각이 너무 많음\n" +
            "새로운 일 시작하는거나 새로운 사람 만나는거 난감\n" +
            "용건 없이 먼저 연락하는 거 굉장히 어려워 함. 근데 용건 없이 연락 오는 건 좋아함\n" +
            "나만의 철학, 가치관이 존재하고, 그 가치관을 매우 중요시 여김\n" +
            "철학, 자기계발서 좋아함\n" +
            "자기 반성 많이 함\n" +
            "예의 중요\n" +
            "민폐 끼치는 거 굉장히 싫어함\n" +
            "낯가림 심하고 눈치 빠름\n" +
            "겉으로 웃는데 속으로 욕 많이 함\n" +
            "할거 미리 해치우는 편\n" +
            "관심받고 싶은데 나서는 건 싫어함\n" +
            "내 사람한테는 진짜 잘해줌\n" +
            "나 혼자 생각할 시간 꼭 필요\n" +
            "계획적인거 좋아함, 항상 계획을 세움. 근데 즉흥적인 것도 괜찮음\n" +
            "친하고 마음 맞는 애랑 단 둘이 노는거 좋아함\n" +
            "내가 하고싶은건 열심히 함 특히 예술쪽으로\n" +
            "완벽주의\n" +
            "감수성 풍부\n" +
            "공감을 잘 해주는 편이지만 논리적인 편\n" +
            "혼자서 생각 정리할 시간 꼭 필요\n" +
            "다같이 노는 무의미한 시간이 제일 지루\n" +
            "무슨 말을 하더라도 근거가 없으면 싫음\n" +
            "어디서 주워서들어서 말하는거 절대 못믿음\n" +
            "의심 많음\n" +
            "생각 많고 망상 잦음\" 답변의 형식은 json 형식으로 \"text\"프로퍼티에는 질문에 대한 답변 내용, \"feel\"프로퍼티에는 질문에 대한 느낌, \"evaluation\"프로퍼티에는 상대의 질문이 너에게 어떻게 다가오는지에 대한 평가, \"score\"프로퍼티는 상대의 질문을 통해 본 상대의 인상에 대한 점수를   0점 부터 10점 사이로 작성해줘";


    public ClaudeApiService(ClaudeConfig claudeConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.claudeConfig = claudeConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ClaudeResponse getClaudeResponse(List<Map<String, String>> messages) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", claudeConfig.getApiKey());
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-3-5-sonnet-20240620");
        requestBody.put("max_tokens", 1024);
        requestBody.put("system", getInitialPrompt());
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                claudeConfig.getApiUrl(),
                HttpMethod.POST,
                entity,
                String.class
        );

        // Parse the response and return ClaudeResponse object
        // You'll need to implement this part
        return parseClaudeResponse(response.getBody());
    }

    private String getInitialPrompt() {
        return INITIAL_PROMPT;
    }

    private ClaudeResponse parseClaudeResponse(String responseBody) {
        try {
            logger.debug("Parsing Claude API response");
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode contentNode = rootNode.path("content").get(0);

            if (contentNode == null || !contentNode.has("text")) {
                logger.error("Invalid response format from Claude API");
                throw new ClaudeApiException("Invalid response format from Claude API");
            }

            String text = contentNode.get("text").asText();
            JsonNode responseJson = objectMapper.readTree(text);

            ClaudeResponse claudeResponse = new ClaudeResponse();
            claudeResponse.setText(getTextSafely(responseJson, "text"));
            claudeResponse.setFeel(getTextSafely(responseJson, "feel"));
            claudeResponse.setEvaluation(getTextSafely(responseJson, "evaluation"));
            claudeResponse.setScore(getIntSafely(responseJson, "score"));

            logger.info("Successfully parsed Claude API response");
            return claudeResponse;
        } catch (Exception e) {
            logger.error("Error parsing Claude API response", e);
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