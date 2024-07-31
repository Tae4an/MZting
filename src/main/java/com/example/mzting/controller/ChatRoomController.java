package com.example.mzting.controller;

import com.example.mzting.dto.ChatRoomRequest;
import com.example.mzting.dto.ChatRoomWithHistoryDTO;
import com.example.mzting.entity.ChatRoom;
import com.example.mzting.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/chatroom/create")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomService.createChatRoom(request);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/chatroom/list/{userId}")
    public ResponseEntity<List<ChatRoom>> getChatRoomList(@PathVariable Long userId) {
        List<ChatRoom> chatRooms = chatRoomService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<ChatRoomWithHistoryDTO> getChatRoomWithHistory(@PathVariable Long chatRoomId) {
        ChatRoomWithHistoryDTO chatRoomWithHistory = chatRoomService.getChatRoomWithHistory(chatRoomId);
        return ResponseEntity.ok(chatRoomWithHistory);
    }
}
