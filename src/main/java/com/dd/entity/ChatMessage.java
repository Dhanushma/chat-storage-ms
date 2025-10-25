package com.dd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    @Column(name = "chat_message_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatMessageId;

    private String messageContent;

    private String sender;

    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_session_id", nullable = false)
    private ChatSession chatSession;

}
