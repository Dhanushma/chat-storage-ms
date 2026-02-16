package com.dd.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {

    @NotBlank(message = "Message content can not be empty")
    private String messageContent;
}
