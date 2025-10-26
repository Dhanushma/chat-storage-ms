package com.dd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionRequest {

    @NotNull(message = "User id can not be blank")
    private long userId;
    @NotBlank(message = "Session name can not be blank")
    private String sessionName;
    private boolean isFavorite;
    private long chatSessionId;
}
