package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

/**
 * 프로필 엔티티 클래스
 * 캐릭터의 프로필 정보를 나타냄
 */
@Setter
@Getter
@Entity
@Table(name = "profile")
@ToString(exclude = {"characterHobbies", "characterKeywords"})
public class Profile {

    // 프로필 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Integer profileId;

    // 캐릭터 이미지 URL
    @Column(name = "character_image", nullable = false)
    private String characterImage;

    // 캐릭터 이름
    @Column(name = "name", nullable = false)
    private String name;

    // MBTI 유형 (4자리)
    @Column(name = "mbti", nullable = false, length = 4)
    private String mbti;

    // 나이
    @Column(name = "age", nullable = false)
    private Integer age;

    // 키 (소수점 두 자리까지)
    @Column(name = "height", nullable = false, precision = 5, scale = 2)
    private BigDecimal height;

    // 직업
    @Column(name = "job", nullable = false)
    private String job;

    // 캐릭터 설명 (텍스트 형식)
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "prompt", columnDefinition = "TEXT")
    private String prompt;

    // 캐릭터와 취미의 일대다 관계
    @OneToMany(mappedBy = "profile")
    private Set<CharacterHobby> characterHobbies;

    // 캐릭터와 키워드의 일대다 관계
    @OneToMany(mappedBy = "profile")
    private Set<CharacterKeyword> characterKeywords;
}
