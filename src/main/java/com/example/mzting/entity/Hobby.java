package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

/**
 * 취미 엔티티 클래스
 * 취미의 정보를 나타냄
 */
@Entity
@Table(name = "hobbies")
public class Hobby {

    // 취미 ID (기본 키)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hobby_id")
    private Integer hobbyId;

    // 취미 이름
    @Getter
    @Column(nullable = false)
    private String hobby;

    // 캐릭터 취미와의 일대다 관계
    @OneToMany(mappedBy = "hobby")
    private Set<CharacterHobby> characterHobbies;
}
