package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "profile")
@ToString(exclude = {"hobbies", "keywords"})
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Integer profileId;

    @Column(name = "character_image", nullable = false)
    private String characterImage;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mbti", nullable = false, length = 4)
    private String mbti;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "height", nullable = false, precision = 5, scale = 2)
    private BigDecimal height;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "profile")
    private Set<CharacterHobby> characterHobbies;

    @OneToMany(mappedBy = "profile")
    private Set<CharacterKeyword> characterKeywords;
}