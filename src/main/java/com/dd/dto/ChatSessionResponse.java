package com.dd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionResponse {
    private long id;
    private long userId;
    private String sessionName;
    private boolean isFavorite;
    private LocalDateTime createdOn;
}
