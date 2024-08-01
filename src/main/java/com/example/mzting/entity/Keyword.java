package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

/**
 * 키워드 엔티티 클래스
 * 키워드의 정보를 나타냄
 */
@Entity
@Table(name = "keywords")
public class Keyword {

    // 키워드 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Integer keywordId;

    // 키워드 이름
    @Getter
    @Column(nullable = false)
    private String keyword;

    // 캐릭터 키워드와의 일대다 관계
    @OneToMany(mappedBy = "keyword")
    private Set<CharacterKeyword> characterKeywords;
}
