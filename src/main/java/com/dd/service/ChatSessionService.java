package com.dd.service;

import com.dd.entity.ChatSession;
import com.dd.repository.ChatMessageRepository;
import com.dd.repository.ChatSessionRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@Transactional
public class ChatSessionService {

 private ChatSessionRepository chatSessionRepository;
 private ChatMessageRepository chatMessageRepository;

 private static final Logger log = LoggerFactory.getLogger(ChatSessionService.class);

    @Autowired
    public ChatSessionService(ChatSessionRepository chatSessionRepository, ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatSessionRepository = chatSessionRepository;
    }

    public ChatSession createChatSession(Long userId, String sessionName) {
        log.info("Create chat session for user : {}, sessionName={}", userId, sessionName);
        // check user existence can be added here
        ChatSession chatSession = ChatSession.builder()
                .userId(userId)
                .sessionName(sessionName)
                .isFavorite(false)
                .build();
        return chatSessionRepository.save(chatSession);
    }

    public ChatSession updateChatSession(ChatSession chatSession) {
        log.info("Updating chat session {}", chatSession);
        return chatSessionRepository.save(chatSession);
    }

    public Optional<ChatSession> getSessionById(long sessionId) {
        log.info("Fetching session info for session id : {}", sessionId);
        return chatSessionRepository.findById(sessionId);
    }

    public void deleteChatSession(long sessionId) {
        log.info("Deleting session and associated chats for session id : {}", sessionId);
        chatMessageRepository.deleteBySessionId(sessionId);
        chatSessionRepository.deleteById(sessionId);
    }

    public Page<ChatSession> getUserChatSessions(Long userId, Pageable pageable) {
        log.info("Fetching paginated chat sessions for user : {}");
        return chatSessionRepository.findByUserId(userId, pageable);
    }
}
