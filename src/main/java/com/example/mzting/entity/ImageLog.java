package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    // 댓글 작성 시간 (생성 시 자동 설정)
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime gnTime;

    @Column(nullable = false)
    private String imageUrl;
}
