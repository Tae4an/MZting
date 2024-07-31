package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mbti_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MBTIPosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false, length = 4)
    private String mbti;

    @Column(nullable = false)
    private Long totalLikeCount;

    @Column(nullable = false)
    private Long totalDislikeCount;
}