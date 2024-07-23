package com.example.mzting.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChatService {
    @Getter
    private List<String> savedResponses = new ArrayList<>();
    private List<String> userRequests = new ArrayList<>();

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
}