package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 사용자 엔티티 클래스
 * 사용자 정보와 관련된 필드를 정의
 */
@Data
@Entity
@Table(name = "users")
public class User {

    // 사용자 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이름 (고유, 필수)
    @Column(unique = true, nullable = false)
    private String username;

    // 사용자 비밀번호 (필수)
    @Column(nullable = false)
    private String password;

    // 사용자 이메일 (필수)
    @Column(nullable = false)
    private String email;

    // 계정 활성화 여부 (기본값 true)
    @Column(nullable = false)
    private boolean enabled = true;

    // 이메일 인증 여부
    @Column(name = "email_verified")
    private boolean emailVerified = false;

    // 이메일 인증 토큰
    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    // 이메일 토큰 만료 날짜
    @Column(name = "email_token_expiry_date")
    private LocalDateTime emailTokenExpiryDate;

    // 생성 시간
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 수정 시간
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 생성 전 호출되는 콜백 메서드
     * 생성 시간과 수정 시간을 현재 시간으로 설정
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 수정 전 호출되는 콜백 메서드
     * 수정 시간을 현재 시간으로 설정
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
