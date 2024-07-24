package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "character_keywords")
public class CharacterKeyword {
    @Id
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Getter
    @Id
    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;


}