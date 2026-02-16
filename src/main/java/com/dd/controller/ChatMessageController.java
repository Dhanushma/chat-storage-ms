package com.dd.controller;

import com.dd.dto.ChatMessageRequest;
import com.dd.dto.ChatMessageResponse;
import com.dd.service.ChatMessageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat-sessions/{sessionId}/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<ChatMessageResponse> processAndSaveMessage(@PathVariable long sessionId,
                                                                     @RequestBody @Valid ChatMessageRequest chatMessageRequest) {
        ChatMessageResponse chatMessageResponse = chatMessageService.processAndSaveMessage(sessionId, chatMessageRequest.getMessageContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(chatMessageResponse);
    }

    @GetMapping
    public ResponseEntity<Page<ChatMessageResponse>> getChatMessages(@PathVariable long sessionId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        Page<ChatMessageResponse> chatMessages = chatMessageService.getMessagesBySessionId(sessionId, PageRequest.of(page, size));
        return ResponseEntity.ok(chatMessages);
    }
}
