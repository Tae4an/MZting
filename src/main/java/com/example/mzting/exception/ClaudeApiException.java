package com.example.mzting.exception;

public class ClaudeApiException extends RuntimeException {
    public ClaudeApiException(String message) {
        super(message);
    }

    public ClaudeApiException(String message, Throwable cause) {
        super(message, cause);
    }
}