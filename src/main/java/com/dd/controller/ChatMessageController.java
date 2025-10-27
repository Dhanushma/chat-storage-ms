package com.dd.controller;

import com.dd.dto.ChatMessageRequest;
import com.dd.dto.ChatMessageResponse;
import com.dd.entity.ChatMessage;
import com.dd.service.ChatMessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatSessions/{sessionId}/chatMessages")
public class ChatMessageController {

    private ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @PostMapping
    public ResponseEntity<ChatMessageResponse> processAndSaveMessage(@RequestBody @Valid ChatMessageRequest chatMessageRequest) {
        ChatMessageResponse chatMessageResponse = chatMessageService.processAndSaveMessage(chatMessageRequest.getSessionId(), chatMessageRequest.getMessageContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(chatMessageResponse);
    }

    @GetMapping
    public ResponseEntity<Page<ChatMessage>> getChatMessages(@RequestParam @NotNull long sessionId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {

        Page<ChatMessage> chatMessages = chatMessageService.getMessagesBySessionId(sessionId, PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(chatMessages);
    }
}
