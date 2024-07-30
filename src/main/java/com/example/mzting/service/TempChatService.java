package com.example.mzting.service;

import com.example.mzting.entity.Chat;

import com.example.mzting.repository.ChatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.mzting.dto.ClaudeResponse;

@Service
public class TempChatService {
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;

    public TempChatService(ChatRepository chatRepository, ObjectMapper objectMapper) {
        this.chatRepository = chatRepository;
        this.objectMapper = objectMapper;
    }

    public Chat saveUserMessage(String content, Long chatRoomId) {
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        chat.setIsBot(false);
        chat.setContent(content);
        chat.setBotInfo(null);
        return chatRepository.save(chat);
    }

    public Chat saveUserMessage(ClaudeResponse content, Long chatRoomId) {
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