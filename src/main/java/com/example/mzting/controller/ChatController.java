package com.example.mzting.controller;

import com.example.mzting.dto.UserMessage;
import com.example.mzting.dto.ClaudeResponse;
import com.example.mzting.entity.Chat;
import com.example.mzting.service.ClaudeApiService;
import com.example.mzting.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ClaudeApiService.class);

    private final ClaudeApiService claudeApiService;
    private final ChatService chatService;

    public ChatController(ClaudeApiService claudeApiService, ChatService chatService) {
        this.claudeApiService = claudeApiService;
        this.chatService = chatService;
    }

    @PostMapping(value = "/ask-claude", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> askClaude(@RequestBody UserMessage userMessageObj) {
        try {
            String userMessage = userMessageObj.getMessage();
            String userMbti = userMessageObj.getMbti();
            Long chatRoomId = 1L; // 추가: 채팅방 ID를 받아옴

            // 사용자 메시지를 DB에 저장
            chatService.saveUserMessage(userMessage, chatRoomId);

            chatService.addUserRequest(userMessage);

            ClaudeResponse claudeResponse = claudeApiService.getClaudeResponseByMbti(
                    userMbti,
                    chatService.getMessagesForClaudeApi()
            );

            chatService.addClaudeResponse(claudeResponse.getText());

            // Claude의 응답도 DB에 저장 (선택적)
            chatService.saveUserMessage(claudeResponse, chatRoomId);

            return ResponseEntity.ok(claudeResponse);
        } catch (Exception e) {
            logger.error("Error processing request", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error processing request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    @GetMapping("/get-responses")
    public ResponseEntity<List<String>> getResponses() {
        return ResponseEntity.ok(chatService.getSavedResponses());
    }

    @GetMapping("/get-conversation")
    public ResponseEntity<List<String>> getConversation() {
        return ResponseEntity.ok(chatService.getConversation());
    }
}