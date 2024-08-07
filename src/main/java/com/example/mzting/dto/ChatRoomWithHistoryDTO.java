package com.example.mzting.dto;

import com.example.mzting.entity.Chat;
import com.example.mzting.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 채팅방과 그 히스토리를 나타내는 DTO 클래스
 * 채팅방 정보와 채팅 히스토리를 포함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomWithHistoryDTO {

    // 채팅방 정보
    private ChatRoom chatRoom;

    // 채팅 히스토리 목록
    private List<Chat> chatHistory;

}
