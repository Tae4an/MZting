package com.example.mzting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {

    @Value("${claude.api.url}")
    private String claudeApiUrl;

    @Value("${claude.api.key}")
    private String apiKey;

    public String getClaudeApiUrl() {
        return claudeApiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
}
