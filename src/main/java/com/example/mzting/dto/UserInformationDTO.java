package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationDTO {
    private Long uid;
    private String username;
    private String email;
    private String provider;
    private boolean emailVerified;

    // uid, username, email을 받는 생성자 추가
    public UserInformationDTO(Long uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    // 기존의 uid와 username만 받는 생성자
    public UserInformationDTO(Long uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    // OAuth2 정보를 위한 생성자
    public UserInformationDTO(String username, String email, String provider, boolean emailVerified) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.emailVerified = emailVerified;
    }
}