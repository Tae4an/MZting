package com.example.mzting.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Claude API 설정 클래스
 * claude.api.url과 claude.api.key 설정을 로드하여 보관
 */
@Getter
@Configuration
public class ClaudeConfig {

    // Claude API의 URL
    @Value("${claude.api.url}")
    private String apiUrl;

    // Claude API의 키
    @Value("${claude.api.key}")
    private String apiKey;

    // Claude API의 고정 프롬프트
    @Value("${claude.fixed-prompt}")
    private String fixedPrompt;
}
