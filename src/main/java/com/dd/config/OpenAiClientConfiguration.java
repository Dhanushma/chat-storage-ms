package com.dd.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiClientConfiguration {

    @Value("${openai.apikey}")
    private String openAiApiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("Authorization", "Bearer " + openAiApiKey);
            template.header("Content-Type", "application/json");
        };
    }
}
