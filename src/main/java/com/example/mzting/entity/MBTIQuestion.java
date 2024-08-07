package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * MBTI 질문 엔티티 클래스
 * MBTI 질문의 정보를 나타냄
 */
@Entity
@Table(name = "mbti_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MBTIQuestion {

    // 질문 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 질문 내용
    private String question;

    // 옵션 A
    private String optionA;

    // 옵션 B
    private String optionB;
}
