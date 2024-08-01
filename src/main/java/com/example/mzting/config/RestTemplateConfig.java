package com.example.mzting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import java.time.Duration;

/**
 * RestTemplate 설정 클래스
 * RestTemplate의 커넥션 타임아웃 및 읽기 타임아웃을 설정
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate 빈을 생성하는 메서드
     * 커넥션 타임아웃과 읽기 타임아웃을 설정하여 RestTemplate 객체를 생성
     *
     * @param builder RestTemplateBuilder 객체
     * @return 설정된 RestTemplate 객체
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                // 커넥션 타임아웃 설정 (10초)
                .setConnectTimeout(Duration.ofSeconds(10))
                // 읽기 타임아웃 설정 (30초)
                .setReadTimeout(Duration.ofSeconds(30))
                // RestTemplate 빌드
                .build();
    }
}
