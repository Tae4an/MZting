package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "character_hobbies")
public class CharacterHobby {
    @Id
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Getter
    @Id
    @ManyToOne
    @JoinColumn(name = "hobby_id")
    private Hobby hobby;

}