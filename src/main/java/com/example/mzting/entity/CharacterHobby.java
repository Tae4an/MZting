package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * 캐릭터 취미 엔티티 클래스
 * 프로필과 취미 간의 관계를 나타냄
 */
@Entity
@Table(name = "character_hobbies")
public class CharacterHobby {

    // 프로필 엔티티
    @Id
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    // 취미 엔티티
    @Getter
    @Id
    @ManyToOne
    @JoinColumn(name = "hobby_id")
    private Hobby hobby;
}
