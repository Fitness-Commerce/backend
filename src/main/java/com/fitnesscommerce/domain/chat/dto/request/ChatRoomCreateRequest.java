package com.fitnesscommerce.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRequest {

    private String roomName;
    private Long buyerId;
    private Long sellerId;
    private String firstMessage;
    private LocalDateTime firstMessageTime;
}
