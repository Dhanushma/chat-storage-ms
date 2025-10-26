package com.dd.service;

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

@Service
@Transactional
public class ChatMessageService {

    private final ChatSessionService chatSessionService;
    private final ChatMessageRepository chatMessageRepository;

    private static final Logger log = LoggerFactory.getLogger(ChatMessageService.class);

    public ChatMessageService(ChatSessionService chatSessionService, ChatMessageRepository chatMessageRepository) {
        this.chatSessionService = chatSessionService;
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveChatMessage(long sessionId, String messageContent, String messageSender) {
        log.info("Saving chat message to session : {}, message content : {}, sender : {}", sessionId, messageContent, messageSender);
        ChatSession chatSession = chatSessionService.getSessionById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));
        ChatMessage chatMessage = ChatMessage.builder()
                .sessionId(chatSession.getId())
                .messageContent(messageContent)
                .messageSender(messageSender)
                .build();
        return chatMessageRepository.save(chatMessage);
    }

    public Page<ChatMessage> getMessagesBySessionId(long sessionId, Pageable pageable) {
        log.info("Fetching chat messages for a session : {}", sessionId);
        return chatMessageRepository.findBySessionIdOrderByCreatedOnAsc(sessionId, pageable);
    }

}
