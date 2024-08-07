package com.example.mzting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 채팅방 생성 요청을 나타내는 DTO 클래스
 * 채팅방 이름, 사용자 ID, 프로필 ID를 포함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequest {

    // 채팅방 이름
    private String name;

    // 사용자 ID
    private Long userId;

    // 프로필 ID
    private Integer profileId;
}
