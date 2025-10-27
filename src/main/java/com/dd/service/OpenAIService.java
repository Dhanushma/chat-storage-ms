package com.dd.service;

import com.dd.client.OpenAIClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Autowired
    private OpenAIClient openAIClient;

    private Logger log = LoggerFactory.getLogger(OpenAIService.class);

    public String getChatResponse(String prompt, List<Map<String, String>> context) {
        log.info("Asking Chatgpt to get response for prompt : {}, context : {}", prompt, context);
        context.add(Map.of("role", "user", "content", prompt));

        try {
            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", context
            );

            Map<String, Object> response = openAIClient.createCompletion(requestBody);
            log.info("Response from Chatgpt : {}", response);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
            log.info("Response from Chatgpt is empty");
            return "No response for the given prompt";
        } catch (FeignException e) {
            if (e.status() == 429) {
                log.error("OpenAI quota exceeded: {}", e.getMessage());
                return "OpenAI quota exceeded. Please try again later.";
            }
            log.error("FeignException calling OpenAI: {}", e.contentUTF8(), e);
            return "Error communicating with OpenAI API.";
        } catch (Exception e) {
            log.error("Unexpected Error occurred while fetching chat response from Chatgpt", e);
            throw new RuntimeException("Unexpected Error occurred while fetching chat response from Chatgpt", e);
        }
    }
}
