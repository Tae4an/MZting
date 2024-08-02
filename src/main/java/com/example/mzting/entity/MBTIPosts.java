package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * MBTI 게시물 엔티티 클래스
 * MBTI 유형과 관련된 게시물의 정보를 나타냄
 */
@Entity
@Table(name = "mbti_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MBTIPosts {

    // 게시물 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    // MBTI 유형 (4자리)
    @Column(nullable = false, length = 4)
    private String mbti;

    // 총 좋아요 개수
    @Column(nullable = false)
    private Long totalLikeCount;

    // 총 싫어요 개수
    @Column(nullable = false)
    private Long totalDislikeCount;
}
