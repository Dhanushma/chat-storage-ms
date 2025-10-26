package com.dd.controller;

import com.dd.dto.ChatSessionRequest;
import com.dd.entity.ChatSession;
import com.dd.exception.ResourceNotFoundException;
import com.dd.service.ChatSessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat-sessions")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    public ChatSessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @PostMapping
    public ResponseEntity<ChatSession> createChatSession(@RequestBody @Valid ChatSessionRequest chatSessionRequest) {
        ChatSession chatSession = chatSessionService.createChatSession(chatSessionRequest.getUserId(), chatSessionRequest.getSessionName());
        return ResponseEntity.status(HttpStatus.CREATED).body(chatSession);
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<ChatSession> updateChatSession(@RequestParam @NotNull long sessionId, @RequestBody @Valid ChatSessionRequest chatSessionRequest) {
        ChatSession chatSession = chatSessionService.getSessionById(sessionId).orElseThrow(() -> new RuntimeException("Chat session not found"));
        chatSession.setSessionName(chatSessionRequest.getSessionName());
        chatSession.setFavorite(chatSessionRequest.isFavorite());
        chatSession.setUserId(chatSessionRequest.getUserId());
        ChatSession updatedSession = chatSessionService.updateChatSession(chatSession);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSession);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ChatSession> getChatSessionById(@PathVariable String sessionId) {
        ChatSession chatSession = chatSessionService.getSessionById(Long.parseLong(sessionId))
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));
        return ResponseEntity.status(HttpStatus.OK).body(chatSession);
    }

    @GetMapping
    public ResponseEntity<Page<ChatSession>> getUserChatSessions(@RequestParam @NotNull long usedId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {

        Page<ChatSession> chatSessions = chatSessionService.getUserChatSessions(usedId, PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(chatSessions);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteChatSession(@PathVariable long sessionId) {
        chatSessionService.deleteChatSession(sessionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
