package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClaudeResponse {
    private String text;
    private String feel;
    private String evaluation;
    private int score;
    private String errorMessage;

    // toString 메서드 오버라이드
    @Override
    public String toString() {
        return "ClaudeResponse{" +
                "text='" + text + '\'' +
                ", feel='" + feel + '\'' +
                ", evaluation='" + evaluation + '\'' +
                ", score=" + score +
                '}';
    }

}
