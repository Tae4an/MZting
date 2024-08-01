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

/**
 * 채팅방 서비스 클래스
 * 채팅방의 생성, 조회 및 관련된 비즈니스 로직을 처리
 */
@Service
public class ChatRoomService {

    // 채팅방 저장소
    private final ChatRoomRepository chatRoomRepository;

    // 채팅 메시지 저장소
    private final ChatRepository chatRepository;

    /**
     * ChatRoomService 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param chatRoomRepository 채팅방 저장소
     * @param chatRepository 채팅 메시지 저장소
     */
    public ChatRoomService(ChatRoomRepository chatRoomRepository, ChatRepository chatRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
    }

    /**
     * 새로운 채팅방을 생성하는 메서드
     *
     * @param request 채팅방 생성 요청 객체
     * @return 생성된 채팅방 객체
     */
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

    /**
     * 채팅방 ID로 채팅방과 그 히스토리를 조회하는 메서드
     *
     * @param id 채팅방 ID
     * @return 채팅방과 히스토리를 포함한 ChatRoomWithHistoryDTO 객체
     * @throws ResourceNotFoundException 채팅방을 찾을 수 없는 경우 예외 발생
     */
    public ChatRoomWithHistoryDTO getChatRoomWithHistory(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found with id: " + id));
        List<Chat> chatHistory = chatRepository.findByChatRoomIdOrderBySendAtAsc(id);
        return new ChatRoomWithHistoryDTO(chatRoom, chatHistory);
    }

    /**
     * 사용자 ID로 채팅방 목록을 조회하는 메서드
     *
     * @param userId 사용자 ID
     * @return 채팅방 목록
     */
    public List<ChatRoom> getChatRoomsByUserId(Long userId) {
        return chatRoomRepository.findByUserId(userId);
    }
}
