package com.example.mzting.service;

import com.example.mzting.dto.ChatRoomEntryResponseDTO;
import com.example.mzting.dto.ChatRoomRequest;
import com.example.mzting.dto.ChatRoomWithHistoryDTO;
import com.example.mzting.entity.Chat;
import com.example.mzting.entity.ChatRoom;
import com.example.mzting.exception.ResourceNotFoundException;
import com.example.mzting.repository.ChatRepository;
import com.example.mzting.repository.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        chatRoom.setMissionFlag(0);
        chatRoom.setIsProcessing(true);
        chatRoom.setResult(null);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoomWithHistoryDTO getChatRoomWithHistory(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found with id: " + chatRoomId));
        List<Chat> chatHistory = chatRepository.findByChatRoomIdOrderBySendAtAsc(chatRoomId);
        return new ChatRoomWithHistoryDTO(chatRoom, chatHistory);
    }

    public List<ChatRoomEntryResponseDTO> getChatRoomEntries(Long chatRoomId) {
        List<Chat> chatHistory = chatRepository.findByChatRoomIdOrderBySendAtAsc(chatRoomId);
        List<ChatRoomEntryResponseDTO> chatRoomEntryResponseDTOs = new ArrayList<>();

        for (Chat chat : chatHistory) {
            String role = chat.getIsBot() ? "assistant" : "user";
            ChatRoomEntryResponseDTO dto = new ChatRoomEntryResponseDTO(
                    role,
                    chat.getContent(),
                    chat.getBotInfo()  // botInfo 추가
            );
            chatRoomEntryResponseDTOs.add(dto);
        }

        return chatRoomEntryResponseDTOs;
    }

    public List<ChatRoom> getChatRoomsByUserId(Long userId) {
        return chatRoomRepository.findByUserId(userId);
    }

    public List<ChatRoom> getChatRoomsByUserIdAndProfileId(Long userId, Integer profileId) {
        return chatRoomRepository.findByUserIdAndProfileId(userId, profileId);
    }

    public boolean isUserAuthorized(Long userId, Long chatRoomId) {
        return chatRoomRepository.existsByIdAndUserId(chatRoomId, userId);
    }
}