package com.dd.service;

import com.dd.dto.ChatMessageResponse;
import com.dd.entity.ChatMessage;
import com.dd.entity.ChatSession;
import com.dd.exception.ResourceNotFoundException;
import com.dd.repository.ChatMessageRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ChatMessageService {

    private final ChatSessionService chatSessionService;
    private final OpenAIService openAIService;
    private final ChatMessageRepository chatMessageRepository;

    private static final Logger log = LoggerFactory.getLogger(ChatMessageService.class);

    public ChatMessageService(ChatSessionService chatSessionService, OpenAIService openAIService, ChatMessageRepository chatMessageRepository) {
        this.chatSessionService = chatSessionService;
        this.openAIService = openAIService;
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessageResponse processAndSaveMessage(long sessionId, String messageContent) {
        log.info("call chatgpt service to fetch AI response for message : {}", messageContent);
        ChatSession chatSession = chatSessionService.getSessionById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));

        // call chatgpt service to fetch AI response with context, message content/prompt
        // fetching latest messages in the session as context
        List<ChatMessage> latestMessages = chatMessageRepository.findTop10BySessionIdOrderByCreatedOnDesc(sessionId);
        List<Map<String, String>> context = new ArrayList<>();

        for (ChatMessage m : latestMessages) {
            context.add(Map.of("role", m.getMessageSender(), "content", m.getMessageContent()));
        }

        //String aiResponse = "hello from chatgpt"; // TODO: remove this line once openAI service is integrated
        String aiResponse = openAIService.getChatResponse(messageContent, context);

        // save prompt and response
        chatMessageRepository.save(ChatMessage.builder()
                .sessionId(chatSession.getId())
                .messageContent(messageContent)
                .messageSender("user")
                .build());
        chatMessageRepository.save(ChatMessage.builder()
                .sessionId(chatSession.getId())
                .messageContent(aiResponse)
                .messageSender("chat-assistant")
                .build());

        return ChatMessageResponse.builder()
                .chatgptResponse(aiResponse)
                .messageContent(messageContent)
                .sessionId(sessionId)
                .build();
    }

    public Page<ChatMessage> getMessagesBySessionId(long sessionId, Pageable pageable) {
        log.info("Fetching chat messages for a session : {}", sessionId);
        return chatMessageRepository.findBySessionIdOrderByCreatedOnAsc(sessionId, pageable);
    }

}
