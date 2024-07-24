package com.example.mzting.dto;

import lombok.Getter;
import lombok.Setter;

public class ClaudeResponse {
    @Setter
    @Getter
    private String text;
    @Setter
    @Getter
    private String feel;
    @Setter
    @Getter
    private String evaluation;
    @Setter
    @Getter
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
