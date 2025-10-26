package com.dd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private String sessionName;

    private LocalDateTime createdOn;

    private boolean isFavorite;

    @PrePersist
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }
}
