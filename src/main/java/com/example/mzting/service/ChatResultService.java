package com.example.mzting.service;

import com.example.mzting.dto.ChatResultResponse;
import com.example.mzting.entity.Chat;
import com.example.mzting.entity.ChatRoom;
import com.example.mzting.repository.ChatRepository;
import com.example.mzting.repository.ChatRoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatResultService {

    private static final Logger logger = LoggerFactory.getLogger(ChatResultService.class);

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${claude.api.url}")
    private String apiUrl;

    @Value("${claude.api.key}")
    private String apiKey;

    private static final String SYSTEM_PROMPT = """
            주어진 데이터는 소개팅 상대의 반응에 대한 데이터입니다., 데이터의 'feel'과 'evaluation' 필드를 분석하여 전반적인 내용을 요약해서 작성해주세요, 감정의 요약은 summaryFeel 필드에, 평가의 요약은 summaryEval 필드에 작성해주세요. \
            긍정적인 요소와 부정적인 요소를 모두 포함하되, 가장 빈번하게 나타나는 주제와 감정에 중점을 두세요. \
            요약은 간결하고 명확해야 하며, 3-5문장 내외로 작성해주세요.
            응답 형식:
            {
              "summaryFeel": "전반적인 감정 요약",
              "summaryEval": "전반적인 평가 요약"
            }""";

    public ChatResultService(ChatRepository chatRepository, ChatRoomRepository chatRoomRepository, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.chatRepository = chatRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public ChatResultResponse getBotMessagesWithSummary(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        if (chatRoom.getResult() != null) {
            try {
                JsonNode resultNode = objectMapper.readTree(chatRoom.getResult());
                // 각 필드가 존재하는지 확인하고, 없으면 기본값을 사용
                int averageScore = resultNode.has("score") ? resultNode.get("score").asInt() : 0;
                String summarizedEvaluation = resultNode.has("summaryEval") ? resultNode.get("summaryEval").asText() : "";
                String summarizedFeel = resultNode.has("summaryFeel") ? resultNode.get("summaryFeel").asText() : "";
                return new ChatResultResponse(averageScore, summarizedEvaluation, summarizedFeel);
            } catch (JsonProcessingException e) {
                logger.error("Error parsing stored result", e);
            }
        }

        // 기존 코드는 그대로 유지
        List<Chat> botChats = chatRepository.findByChatRoomIdAndIsBotTrueOrderBySendAtAsc(chatRoomId);

        int averageScore = (int) Math.round(calculateAverageScore(botChats));
        String combinedFeel = combineFeel(botChats);
        String combinedEvaluation = combineEvaluation(botChats);

        Map<String, String> summary = getSummary(combinedFeel, combinedEvaluation);

        String summarizedFeel = summary.get("summaryFeel");
        String summarizedEvaluation = summary.get("summaryEval");

        ChatResultResponse response = new ChatResultResponse(averageScore, summarizedEvaluation, summarizedFeel);

        // 결과를 ChatRoom에 저장할 때 구조 변경
        try {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("score", averageScore);
            resultMap.put("summaryEval", summarizedEvaluation);
            resultMap.put("summaryFeel", summarizedFeel);
            String resultJson = objectMapper.writeValueAsString(resultMap);
            chatRoom.setResult(resultJson);
            chatRoomRepository.save(chatRoom);
        } catch (JsonProcessingException e) {
            logger.error("Error storing result", e);
        }

        return response;
    }

    private double calculateAverageScore(List<Chat> chats) {
        OptionalDouble average = chats.stream()
                .mapToDouble(chat -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(chat.getBotInfo());
                        return jsonNode.get("score").asDouble();
                    } catch (Exception e) {
                        return 0.0;
                    }
                })
                .average();
        return average.orElse(0.0);
    }

    private String combineFeel(List<Chat> chats) {
        return chats.stream()
                .map(chat -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(chat.getBotInfo());
                        return jsonNode.get("feel").asText();
                    } catch (Exception e) {
                        return "";
                    }
                })
                .collect(Collectors.joining(", "));
    }

    private String combineEvaluation(List<Chat> chats) {
        return chats.stream()
                .map(chat -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(chat.getBotInfo());
                        return jsonNode.get("evaluation").asText();
                    } catch (Exception e) {
                        return "";
                    }
                })
                .collect(Collectors.joining(" "));
    }

    private Map<String, String> getSummary(String feel, String evaluation) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-3-5-sonnet-20240620");
        requestBody.put("max_tokens", 1024);
        requestBody.put("system", SYSTEM_PROMPT);
        requestBody.put("messages", Collections.singletonList(
                Map.of("role", "user", "content", "feel: " + feel + "\nevaluation: " + evaluation)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return parseClaudeResponse(response.getBody());
    }

    private Map<String, String> parseClaudeResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            String content = rootNode.path("content").get(0).path("text").asText();

            // 로그를 추가하여 실제 응답 내용을 확인
            logger.debug("Claude API raw response content: {}", content);

            Map<String, String> result = new HashMap<>();

            // JSON 파싱을 시도하되, 실패하면 텍스트 그대로 사용
            try {
                JsonNode responseJson = objectMapper.readTree(content);
                result.put("summaryFeel", responseJson.path("summaryFeel").asText());
                result.put("summaryEval", responseJson.path("summaryEval").asText());
            } catch (JsonProcessingException e) {
                // JSON 파싱 실패 시 전체 텍스트를 summaryEval에 넣음
                result.put("summaryFeel", "");
                result.put("summaryEval", content);
            }

            return result;
        } catch (Exception e) {
            logger.error("Error parsing Claude API response", e);
            throw new RuntimeException("Failed to parse Claude API response", e);
        }
    }
}