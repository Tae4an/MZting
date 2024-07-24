package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "hobbies")
public class Hobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hobby_id")
    private Integer hobbyId;

    @Getter
    @Column(nullable = false)
    private String hobby;

    @OneToMany(mappedBy = "hobby")
    private Set<CharacterHobby> characterHobbies;

}