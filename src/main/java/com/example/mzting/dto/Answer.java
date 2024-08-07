package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * MBTI 질문에 대한 답변을 나타내는 DTO 클래스
 * 질문 ID와 사용자가 선택한 옵션을 포함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    // 질문 ID
    private Long questionId;

    // 사용자가 선택한 옵션
    private String option;
}
