package com.example.mzting.dto;

import com.example.mzting.entity.Chat;
import com.example.mzting.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 채팅방과 그 히스토리를 나타내는 DTO 클래스
 * 채팅방 정보와 채팅 히스토리를 포함
 */
@Getter
@Setter
public class ChatRoomWithHistoryDTO {

    // 채팅방 정보
    private ChatRoom chatRoom;

    // 채팅 히스토리 목록
    private List<Chat> chatHistory;

    /**
     * ChatRoomWithHistoryDTO 생성자
     *
     * @param chatRoom 채팅방 정보
     * @param chatHistory 채팅 히스토리 목록
     */
    public ChatRoomWithHistoryDTO(ChatRoom chatRoom, List<Chat> chatHistory) {
        this.chatRoom = chatRoom;
        this.chatHistory = chatHistory;
    }
}
