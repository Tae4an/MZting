package com.example.mzting.controller;

import com.example.mzting.dto.UserMessage;
import com.example.mzting.dto.ClaudeResponse;
import com.example.mzting.service.ClaudeApiService;
import com.example.mzting.service.ChatService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {
    private final ClaudeApiService claudeApiService;
    private final ChatService conversationService;

    public ChatController(ClaudeApiService claudeApiService, ChatService conversationService) {
        this.claudeApiService = claudeApiService;
        this.conversationService = conversationService;
    }

    @PostMapping(value = "/ask-claude", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClaudeResponse> askClaude(@RequestBody UserMessage userMessageObj) {
        String userMessage = userMessageObj.getMessage();
        conversationService.addUserRequest(userMessage);

        ClaudeResponse claudeResponse = claudeApiService.getClaudeResponse(
                conversationService.getMessagesForClaudeApi()
        );

        conversationService.addClaudeResponse(claudeResponse.getText());

        return ResponseEntity.ok(claudeResponse);
    }

    @GetMapping("/get-responses")
    public ResponseEntity<List<String>> getResponses() {
        return ResponseEntity.ok(conversationService.getSavedResponses());
    }

    @GetMapping("/get-conversation")
    public ResponseEntity<List<String>> getConversation() {
        return ResponseEntity.ok(conversationService.getConversation());
    }
}