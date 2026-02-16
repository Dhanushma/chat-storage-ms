package com.dd.service;

import com.dd.client.OpenAIClient;
import com.dd.exception.OpenAIServiceException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private final OpenAIClient openAIClient;
    private final String model;

    private static final Logger log = LoggerFactory.getLogger(OpenAIService.class);

    public OpenAIService(OpenAIClient openAIClient, @Value("${openai.model:gpt-3.5-turbo}") String model) {
        this.openAIClient = openAIClient;
        this.model = model;
    }

    public String getChatResponse(String prompt, List<Map<String, String>> context) {
        log.info("Requesting OpenAI response for session context with {} messages", context.size());

        List<Map<String, String>> messages = new java.util.ArrayList<>(context);
        messages.add(Map.of("role", "user", "content", prompt));

        try {
            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", messages
            );

            Map<String, Object> response = openAIClient.createCompletion(requestBody);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
            throw new OpenAIServiceException("Empty response from OpenAI");
        } catch (FeignException e) {
            if (e.status() == 429) {
                log.error("OpenAI quota exceeded: {}", e.getMessage());
                throw new OpenAIServiceException("OpenAI quota exceeded. Please try again later.");
            }
            log.error("FeignException calling OpenAI: {}", e.contentUTF8(), e);
            throw new OpenAIServiceException("Error communicating with OpenAI API.", e);
        }
    }
}
