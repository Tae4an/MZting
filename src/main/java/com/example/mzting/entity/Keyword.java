package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "keywords")
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Integer keywordId;

    @Getter
    @Column(nullable = false)
    private String keyword;

    @OneToMany(mappedBy = "keyword")
    private Set<CharacterKeyword> characterKeywords;

}