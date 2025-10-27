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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String messageContent;

    private String messageSender;

    private LocalDateTime createdOn;

    private long sessionId;

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }
}
