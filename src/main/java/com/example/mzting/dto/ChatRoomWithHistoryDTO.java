package com.example.mzting.DTO;

import com.example.mzting.entity.Chat;
import com.example.mzting.entity.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomWithHistoryDTO {
    private ChatRoom chatRoom;
    private List<Chat> chatHistory;

    public ChatRoomWithHistoryDTO(ChatRoom chatRoom, List<Chat> chatHistory) {
        this.chatRoom = chatRoom;
        this.chatHistory = chatHistory;
    }
}