package com.example.mzting.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    private Long questionId;
    private String option;

    // Getters and Setters
}
