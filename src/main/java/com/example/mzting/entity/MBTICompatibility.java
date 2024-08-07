package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * MBTI 호환성 엔티티 클래스
 * MBTI 유형 간의 호환성 점수를 나타냄
 */
@Entity
@Table(name = "mbti_compatibility")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MBTICompatibility {

    // MBTI 유형 (기본 키)
    @Id
    @Column(name = "mbti")
    private String mbti;

    // 각 MBTI 유형과의 호환성 점수
    private int infp;
    private int enfp;
    private int infj;
    private int enfj;
    private int intj;
    private int entj;
    private int intp;
    private int entp;
    private int isfp;
    private int esfp;
    private int istp;
    private int estp;
    private int isfj;
    private int esfj;
    private int istj;
    private int estj;
}
