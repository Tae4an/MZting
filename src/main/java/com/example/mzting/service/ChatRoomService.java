package com.example.mzting.service;

import com.example.mzting.dto.ChatRoomRequest;
import com.example.mzting.dto.ChatRoomWithHistoryDTO;
import com.example.mzting.entity.Chat;
import com.example.mzting.entity.ChatRoom;
import com.example.mzting.exception.ResourceNotFoundException;
import com.example.mzting.repository.ChatRepository;
import com.example.mzting.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ChatRepository chatRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
    }

    public ChatRoom createChatRoom(ChatRoomRequest request) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(request.getName());
        chatRoom.setUserId(request.getUserId());
        chatRoom.setProfileId(request.getProfileId());
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoomWithHistoryDTO getChatRoomWithHistory(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found with id: " + id));
        List<Chat> chatHistory = chatRepository.findByChatRoomIdOrderBySendAtAsc(id);
        return new ChatRoomWithHistoryDTO(chatRoom, chatHistory);
    }

    public List<ChatRoom> getChatRoomsByUserId(Long userId) {
        return chatRoomRepository.findByUserId(userId);
    }

}