package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mbti_compatibility")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MBTICompatibility {
    @Id
    @Column(name = "mbti")
    private String mbti;

    private int infp;
    private int enfp;
    private int infj;
    private int enfj;
    private int intj;
    private int entj;
    private int intp;
    private int entp;
    private int isfp;
    private int esfp;
    private int istp;
    private int estp;
    private int isfj;
    private int esfj;
    private int istj;
    private int estj;
}
