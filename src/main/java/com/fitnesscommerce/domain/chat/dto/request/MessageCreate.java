package com.fitnesscommerce.domain.chat.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class MessageCreate {

    private String memberName; //안보내도 됌
    private String message; //보내야함
    private LocalDateTime messageTime; //안보내도 댐
    private String roomName; //보내야함

    @Builder
    public MessageCreate(String memberName, String message, String roomName, LocalDateTime messageTime) {
        this.memberName = memberName;
        this.message = message;
        this.messageTime = messageTime;
        this.roomName = roomName;
    }


}
