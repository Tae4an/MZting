package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMessage {
    private String message;
    private String mbti;
    private Long chatRoomId;
}