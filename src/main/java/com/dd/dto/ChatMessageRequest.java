package com.dd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    @NotNull(message = "Session Id can not be empty")
    private long sessionId;
    @NotBlank(message = "Message content can not be empty")
    private String messageContent;
    @NotBlank(message = "Message sender content can not be empty")
    private String messageSender;
}
