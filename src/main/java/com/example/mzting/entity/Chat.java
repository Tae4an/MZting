package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 채팅 엔티티 클래스
 * 채팅 메시지의 정보를 나타냄
 */
@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chat {

    // 채팅 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅방 ID
    @Column
    private Long chatRoomId;

    // 봇 여부 (봇 메시지 여부)
    @Column(nullable = false)
    private Boolean isBot;

    // 메시지 내용
    @Column(nullable = false)
    private String content;

    // 봇 정보 (JSON 형식)
    @Column(columnDefinition = "json")
    private String botInfo;

    // 미션 번호
    @Column
    private Integer mission;

    // 메시지 전송 시간 (생성 시 자동 설정)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime sendAt;
}
