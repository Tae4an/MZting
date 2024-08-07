package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 메시지를 나타내는 DTO 클래스
 * 메시지 내용, MBTI 유형, 채팅방 ID를 포함
 */
@Getter
@Setter
public class UserMessage {

    // 메시지 내용
    private String message;

    // 사용자 MBTI 유형
    private String mbti;

    // 채팅방 ID
    private Long chatRoomId;
}
