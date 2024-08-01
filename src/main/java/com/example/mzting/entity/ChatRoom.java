package com.example.mzting.entity;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 채팅방 엔티티 클래스
 * 채팅방의 정보를 나타냄
 */
@Entity
@Table(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    // 채팅방 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅방 이름
    @Column(nullable = false)
    private String name;

    // 사용자 ID
    @Column(nullable = false)
    private Long userId;

    // 생성 시간 (생성 시 자동 설정)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createTime;

    // 처리 중 여부 (기본값 true)
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean isProcessing;

    // 미션 플래그
    @Column(nullable = false)
    private Integer missionFlag;

    // 프로필 ID
    @Column(nullable = false)
    private Integer profileId;

    // 결과 (JSON 형식)
    @Column(columnDefinition = "json")
    private String result;
}
