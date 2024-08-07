package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 선택지를 나타내는 엔티티 클래스
 * 상황과 그 선택지의 관계를 정의
 */
@Entity
public class Choice {

    /**
     * -- GETTER --
     *  선택지 ID를 반환
     *
     */
    // 선택지 ID (기본 키)
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer choiceId;

    // 상황 엔티티 (지연 로딩)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "situation_id")
    private Situation situation;

    // 선택지 설명
    private String description;

    /**
     * -- GETTER --
     *  다음 상황을 반환
     *
     */
    // 다음 상황 엔티티 (지연 로딩)
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_situation_id")
    private Situation nextSituation;

}
