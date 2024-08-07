package com.example.mzting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 채팅 결과 응답을 나타내는 DTO 클래스
 * 채팅 점수, 평가 요약, 느낌 요약을 포함
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResultResponse {

    // 채팅 점수
    private Integer score;

    // 평가 요약
    private String summaryEval;

    // 느낌 요약
    private String summaryFeel;
}
