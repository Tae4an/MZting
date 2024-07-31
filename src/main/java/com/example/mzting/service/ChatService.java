package com.example.mzting.service;

import com.example.mzting.entity.Chat;
import com.example.mzting.repository.ChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * ChatService 클래스
 * 사용자와 Claude AI 간의 대화를 관리하는 서비스 클래스.
 * 사용자 요청과 AI 응답을 저장하고, 전체 대화를 반환하며,
 * Claude API에 전달할 메시지 형식으로 변환하는 기능을 제공.
 */
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;
    @Getter
    private List<String> savedResponses = new ArrayList<>(); // 저장된 응답 리스트
    private List<String> userRequests = new ArrayList<>(); // 사용자 요청 리스트
    private Map<String, String> context = new HashMap<>();

    public ChatService(ChatRepository chatRepository, ObjectMapper objectMapper) {
        this.chatRepository = chatRepository;
        this.objectMapper = objectMapper;
    }

    public void addUserRequest(String request) {
        userRequests.add(request);
    }

    /**
     * Claude의 응답을 추가하는 메서드
     * @param response Claude의 응답 문자열
     */
    public void addClaudeResponse(String response) {
        savedResponses.add(response);
    }

    /**
     * 전체 대화를 반환하는 메서드
     * @return 사용자 요청과 Claude 응답이 순서대로 포함된 대화 리스트
     */
    public List<String> getConversation() {
        List<String> conversation = new ArrayList<>();
        for (int i = 0; i < userRequests.size(); i++) {
            conversation.add("User: " + userRequests.get(i));
            if (i < savedResponses.size()) {
                conversation.add("Claude: " + savedResponses.get(i));
            }
        }
        return conversation;
    }

    /**
     * Claude API에 전달할 메시지 형식으로 변환하는 메서드
     * @return 사용자 요청과 Claude 응답을 포함한 메시지 리스트
     */
    public List<Map<String, String>> getMessagesForClaudeApi() {
        List<Map<String, String>> messages = new ArrayList<>();
        for (int i = 0; i < userRequests.size(); i++) {
            messages.add(Map.of("role", "user", "content", userRequests.get(i)));
            if (i < savedResponses.size()) {
                messages.add(Map.of("role", "assistant", "content", savedResponses.get(i)));
            }
        }
        return messages;
    }

    public Chat saveUserMessage(String content, Long chatRoomId) {
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        chat.setIsBot(false);
        chat.setContent(content);
        chat.setBotInfo(null);
        return chatRepository.save(chat);
    }

    public Chat saveUserMessage(com.example.mzting.dto.ClaudeResponse content, Long chatRoomId) {
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        chat.setIsBot(true);
        chat.setContent(content.getText());
        try {
            // ClaudeResponse 객체를 JSON으로 변환
            String jsonString = objectMapper.writeValueAsString(content);
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            // text 필드 제거
            ((ObjectNode) jsonNode).remove("text");
            ((ObjectNode) jsonNode).remove("errorMessage");

            // 남은 정보를 botInfo에 저장
            chat.setBotInfo(jsonNode.toString());
        } catch (JsonProcessingException e) {
            chat.setBotInfo(null);
            e.printStackTrace();
        }

        return chatRepository.save(chat);
    }

    public void addContext(String key, String value) {
        context.put(key, value);
    }

    public String getContext(String key) {
        return context.getOrDefault(key, "");
    }

    public Map<String, String> getFullContext() {
        return new HashMap<>(context);
    }
}