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

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;
    @Getter
    private List<String> savedResponses = new ArrayList<>();
    private List<String> userRequests = new ArrayList<>();

    public ChatService(ChatRepository chatRepository, ObjectMapper objectMapper) {
        this.chatRepository = chatRepository;
        this.objectMapper = objectMapper;
    }

    public void addUserRequest(String request) {
        userRequests.add(request);
    }

    public void addClaudeResponse(String response) {
        savedResponses.add(response);
    }

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
}