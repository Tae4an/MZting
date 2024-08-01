package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 캐릭터 키워드 엔티티 클래스
 * 프로필과 키워드 간의 관계를 나타냄
 */
@Entity
@Table(name = "character_keywords")
public class CharacterKeyword {

    // 프로필 엔티티
    @Id
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    // 키워드 엔티티
    @Getter
    @Id
    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;
}
