package com.example.mzting.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer situationId;

    private Integer chapter;
    private String place;

    @OneToMany(mappedBy = "situation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Choice> choices = new ArrayList<>();

    public Collection<Choice> getChoices() {
        return choices;
    }

}