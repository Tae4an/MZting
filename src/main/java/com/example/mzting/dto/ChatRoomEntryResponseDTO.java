package com.example.mzting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChatRoomEntryResponseDTO {
    private String role;
    private String content;
    private String botInfo;

    public ChatRoomEntryResponseDTO(String role, String content, String botInfo) {
        this.role = role;
        this.content = content;
        this.botInfo = botInfo;
    }

}