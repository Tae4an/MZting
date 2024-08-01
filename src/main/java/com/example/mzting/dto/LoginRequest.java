package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 요청 정보를 나타내는 DTO 클래스
 */
@Getter
@Setter
public class LoginRequest {

    // 사용자 이름
    private String username;

    // 사용자 비밀번호
    private String password;
}
