package com.example.mzting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "mbti_question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MBTIQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    private String optionA;
    private String optionB;
}
