package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 상황 엔티티 클래스
 * 특정 상황의 정보를 나타내고, 해당 상황의 선택지를 포함
 */
@Entity
public class Situation {

    // 상황 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer situationId;

    // 챕터 번호
    private Integer chapter;

    // 장소
    private String place;

    // 상황에 속한 선택지들 (일대다 관계)
    @Getter
    @OneToMany(mappedBy = "situation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Choice> choices = new ArrayList<>();
}
