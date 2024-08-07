package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 댓글 엔티티 클래스
 * 댓글의 정보를 나타냄
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    // 댓글 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId; // 이거 id로 바꿔야 댐

    // 사용자 ID
    @Column(nullable = false)
    private Long userId;

    // 게시물 ID
    @Column(nullable = false)
    private Long profileId;

    // 댓글 내용
    @Column(nullable = false)
    private String content;

    // 좋아요 여부 (기본값 false)
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isLike;

    // 댓글 작성 시간 (생성 시 자동 설정)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime cwTime;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long likeCount;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long dislikeCount;
}
