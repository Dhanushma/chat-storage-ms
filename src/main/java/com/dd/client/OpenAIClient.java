package com.dd.client;

import com.dd.config.OpenAiClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "openAiClient",
        url = "${openai.url}",
        configuration = OpenAiClientConfiguration.class
)
public interface OpenAIClient {

    @PostMapping("/chat/completions")
    Map<String, Object> createCompletion(@RequestBody Map<String, Object> request);
}

