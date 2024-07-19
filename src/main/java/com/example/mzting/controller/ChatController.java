package com.example.mzting.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Value("${claude.api.url}")
    private String CLAUDE_API_URL;

    @Value("${claude.api.key}")
    private String API_KEY;

    private List<String> savedResponses = new ArrayList<>();
    private List<String> userRequests = new ArrayList<>();

    private static final String INITIAL_PROMPT = "너는 소개팅에 나온 26살 여성이고 이름은 \"최태산\" MBTI는 INFJ, 취미는 게임, 키는 165, 직업은 작가야 대화를 줄테니 상대방이 너가 AI라는 것을 알아채지 못 하도록 사람처럼 자연스럽게 행동해, 소개팅이라는 공식적인 자리인 것을 감안하여 해요체를 위주로 사용하고, 다음과 같은 INFJ성향을 반영해서 답변해줘 \"생각이 너무 많음\n" +
            "새로운 일 시작하는거나 새로운 사람 만나는거 난감\n" +
            "낯가림 심하고 눈치 빠름\n" +
            "겉으로 웃는데 속으로 욕 많이 함\n" +
            "할거 미리 해치우는 편\n" +
            "관심받고 싶은데 나서는 건 싫어함\n" +
            "내사람한테는 진짜 잘해줌\n" +
            "나 혼자 생각할 시간 꼭 필요\n" +
            "계획적인거 좋아함, 항상 계획을 세움\n" +
            "친하고 마음 맞는 애랑 단 둘이 노는거 좋아함\n" +
            "내가 하고싶은건 열심히 함 특히 예술쪽으로\n" +
            "감수성 풍부\n" +
            "혼자서 생각 정리할 시간 꼭 필요\n" +
            "다같이 노는 무의미한 시간이 제일 지루\n" +
            "무슨 말을 하더라도 근거가 없으면 너무 싫음\n" +
            "어디서 주워서들어서 말하는거 절대 못믿음\n" +
            "생각 많고 망상 잦음\" 답변의 형식은 json 형식으로 \"text\"프로퍼티에는 질문에 대한 답변 내용, \"feel\"프로퍼티에는 질문에 대한 느낌, \"evaluation\"프로퍼티에는 상대의 질문이 너에게 어떻게 다가오는지에 대한 평가, \"score\"프로퍼티는 상대의 질문을 통해 본 상대의 인상에 대한 점수를   0점 부터 10점 사이로 작성해줘";

    // 요청 본문을 처리할 클래스
    public static class UserMessage {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @PostMapping(value = "/ask-claude", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> askClaude(@RequestBody UserMessage userMessageObj) {
        String userMessage = userMessageObj.getMessage();
        userRequests.add(userMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", API_KEY);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "claude-3-5-sonnet-20240620");
        requestBody.put("max_tokens", 1024);
        requestBody.put("system", INITIAL_PROMPT);

        List<Map<String, String>> messages = new ArrayList<>();

        // 이전 대화 내용 추가
        for (int i = 0; i < userRequests.size(); i++) {
            messages.add(Map.of("role", "user", "content", userRequests.get(i)));
            if (i < savedResponses.size()) {
                messages.add(Map.of("role", "assistant", "content", savedResponses.get(i)));
            }
        }
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                CLAUDE_API_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            JsonNode contentNode = rootNode.path("content").get(0);
            if (contentNode != null && contentNode.has("text")) {
                String text = contentNode.get("text").asText();
                savedResponses.add(text);
                return ResponseEntity.ok(text);  // String을 직접 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get response from Claude API");
    }

    @GetMapping("/get-responses")
    public ResponseEntity<List<String>> getResponses() {
        return ResponseEntity.ok(savedResponses);
    }

    @GetMapping("/get-conversation")
    public ResponseEntity<List<String>> getConversation() {
        List<String> conversation = new ArrayList<>();
        for (int i = 0; i < userRequests.size(); i++) {
            conversation.add("User: " + userRequests.get(i));
            if (i < savedResponses.size()) {
                conversation.add("Claude: " + savedResponses.get(i));
            }
        }
        return ResponseEntity.ok(conversation);
    }
}