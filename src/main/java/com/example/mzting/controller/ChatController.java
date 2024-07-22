package com.example.mzting.controller;

import com.example.mzting.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private ChatService claudeService;

    private List<String> savedResponses = new ArrayList<>();
    private List<String> userRequests = new ArrayList<>();

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

    @PostMapping(value = "/ask-claude", consumes = "application/json")
    public ResponseEntity<String> askClaude(@RequestBody UserMessage userMessageObj) {
        String userMessage = userMessageObj.getMessage();
        userRequests.add(userMessage);

        String response = claudeService.askClaude(userMessage, userRequests, savedResponses);
        savedResponses.add(response);

        return ResponseEntity.ok(response);
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
