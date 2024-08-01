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

import java.util.Arrays;
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
            Long chatRoomId = userMessageObj.getChatRoomId();

            // 사용자 메시지를 DB에 저장
            chatService.saveUserMessage(userMessage, chatRoomId);

            chatService.addUserRequest(userMessage);

            ClaudeResponse claudeResponse = claudeApiService.getClaudeResponseByMbti(
                    userMbti,
                    chatService.getMessagesForClaudeApi()
            );

            chatService.addClaudeResponse(claudeResponse.getText());

            // Claude의 응답도 DB에 저장
            chatService.saveUserMessage(claudeResponse, chatRoomId);

            Map<String, Object> response = new HashMap<>();
            response.put("claudeResponse", claudeResponse);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error processing request", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error processing request: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    private void updateContext(String userMessage, String aiResponse) {
        // 사용자 메시지와 AI 응답을 분석하여 컨텍스트 업데이트
        // 예: 선호하는 음식, 취미 등을 추출하여 저장
        if (userMessage.contains("커피")) {
            chatService.addContext("preference", "커피");
        }
        // 더 많은 컨텍스트 추출 로직 추가
    }

    @GetMapping("/get-responses")
    public ResponseEntity<List<String>> getResponses() {
        return ResponseEntity.ok(chatService.getSavedResponses());
    }

    @GetMapping("/get-conversation")
    public ResponseEntity<List<String>> getConversation() {
        return ResponseEntity.ok(chatService.getConversation());
    }

    @PostMapping("/initialize-chat")
    public ResponseEntity<?> initializeChat(@RequestBody UserMessage userMessageObj) {
        logger.info("Initializing chat for user message: {}", userMessageObj);

        try {
            String userMbti = userMessageObj.getMbti();
            Long chatRoomId = userMessageObj.getChatRoomId(); // 채팅방 ID를 UserMessage에서 받아오도록 수정

            if (userMbti == null || userMbti.isEmpty()) {
                logger.warn("MBTI is null or empty");
                return ResponseEntity.badRequest().body("MBTI is required");
            }

            if (chatRoomId == null) {
                logger.warn("ChatRoomId is null");
                return ResponseEntity.badRequest().body("ChatRoomId is required");
            }

            if (!chatService.isInitialized(chatRoomId)) {
                logger.info("Initializing chat for MBTI: {} and ChatRoomId: {}", userMbti, chatRoomId);
                ClaudeResponse claudeResponse = claudeApiService.initializeClaudeChat(userMbti);
                chatService.saveUserMessage(claudeResponse, chatRoomId);
                chatService.setInitialized(chatRoomId, true);

                Map<String, Object> response = new HashMap<>();
                response.put("claudeResponse", claudeResponse);
                logger.info("Chat initialized successfully for ChatRoomId: {}", chatRoomId);
                return ResponseEntity.ok(response);
            } else {
                logger.info("Chat already initialized for ChatRoomId: {}", chatRoomId);
                return ResponseEntity.ok("Chat already initialized");
            }
        } catch (Exception e) {
            logger.error("Error initializing chat", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error initializing chat: " + e.getMessage());
        }
    }
}