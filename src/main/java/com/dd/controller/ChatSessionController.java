package com.dd.controller;

import com.dd.dto.ChatSessionRequest;
import com.dd.dto.ChatSessionResponse;
import com.dd.entity.ChatSession;
import com.dd.exception.ResourceNotFoundException;
import com.dd.service.ChatSessionService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ChatSessionResponse> createChatSession(@RequestBody @Valid ChatSessionRequest chatSessionRequest) {
        ChatSession chatSession = chatSessionService.createChatSession(chatSessionRequest.getUserId(), chatSessionRequest.getSessionName());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(chatSession));
    }

    @PutMapping("/{sessionId}")
    public ResponseEntity<ChatSessionResponse> updateChatSession(@PathVariable long sessionId,
                                                                 @RequestBody @Valid ChatSessionRequest chatSessionRequest) {
        ChatSession chatSession = chatSessionService.getSessionById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));
        chatSession.setSessionName(chatSessionRequest.getSessionName());
        chatSession.setFavorite(chatSessionRequest.isFavorite());
        chatSession.setUserId(chatSessionRequest.getUserId());
        ChatSession updatedSession = chatSessionService.updateChatSession(chatSession);
        return ResponseEntity.ok(toResponse(updatedSession));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ChatSessionResponse> getChatSessionById(@PathVariable long sessionId) {
        ChatSession chatSession = chatSessionService.getSessionById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));
        return ResponseEntity.ok(toResponse(chatSession));
    }

    @GetMapping
    public ResponseEntity<Page<ChatSessionResponse>> getUserChatSessions(@RequestParam Long userId,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        Page<ChatSessionResponse> chatSessions = chatSessionService.getUserChatSessions(userId, PageRequest.of(page, size))
                .map(this::toResponse);
        return ResponseEntity.ok(chatSessions);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteChatSession(@PathVariable long sessionId) {
        chatSessionService.deleteChatSession(sessionId);
        return ResponseEntity.noContent().build();
    }

    private ChatSessionResponse toResponse(ChatSession chatSession) {
        return ChatSessionResponse.builder()
                .id(chatSession.getId())
                .userId(chatSession.getUserId())
                .sessionName(chatSession.getSessionName())
                .isFavorite(chatSession.isFavorite())
                .createdOn(chatSession.getCreatedOn())
                .build();
    }
}
