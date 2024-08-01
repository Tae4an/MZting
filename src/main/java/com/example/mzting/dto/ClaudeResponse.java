package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Claude API의 응답을 나타내는 DTO 클래스
 * 텍스트, 느낌, 평가, 점수, 단계 완료 상태, 오류 메시지를 포함
 */
@Setter
@Getter
public class ClaudeResponse {

    // 응답 텍스트
    private String text;

    // 느낌 요약
    private String feel;

    // 평가 요약
    private String evaluation;

    // 점수
    private int score;

    // 단계 1 완료 상태
    private boolean stage1Complete;

    // 단계 2 완료 상태
    private boolean stage2Complete;

    // 단계 3 완료 상태
    private boolean stage3Complete;

    // 오류 메시지
    private String errorMessage;

    /**
     * 객체의 문자열 표현을 반환
     *
     * @return 객체의 문자열 표현
     */
    @Override
    public String toString() {
        return "ClaudeResponse{" +
                "text='" + text + '\'' +
                ", feel='" + feel + '\'' +
                ", evaluation='" + evaluation + '\'' +
                ", score=" + score +
                ", stage1Complete=" + stage1Complete +
                ", stage2Complete=" + stage2Complete +
                ", stage3Complete=" + stage3Complete +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
