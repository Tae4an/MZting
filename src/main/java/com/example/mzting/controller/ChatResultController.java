package com.example.mzting.controller;

import com.example.mzting.dto.ChatResultResponse;
import com.example.mzting.entity.Chat;
import com.example.mzting.service.ChatResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatResultController {
    private final ChatResultService chatResultService;

    public ChatResultController(ChatResultService chatResultService) {
        this.chatResultService = chatResultService;
    }

    @GetMapping("chat/result/{chatRoomId}")
    public ChatResultResponse getChatSummary(@PathVariable Long chatRoomId) {
        return chatResultService.getBotMessagesWithSummary(chatRoomId);
    }
}
