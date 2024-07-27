package com.example.mzting.entity;

import jakarta.persistence.*;

@Entity
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer choiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "situation_id")
    private Situation situation;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_situation_id")
    private Situation nextSituation;

    public Integer getChoiceId() {
        return choiceId;
    }

    public Situation getNextSituation() {
        return nextSituation;
    }

}