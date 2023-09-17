package com.fitnesscommerce.domain.chat.dto.response;

import com.fitnesscommerce.domain.chat.domain.ChatMessage;
import com.fitnesscommerce.domain.member.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessagesResponse {

    private String senderNickname;
    private String message;
    private LocalDateTime created_at;

    public MessagesResponse(ChatMessage chatMessage, Member member) {

        String senderName = chatMessage.getSender().getNickname();

        this.senderNickname = (senderName == member.getNickname()) ? (senderName + " 본인") : senderName;
        this.message = chatMessage.getMessage();
        this.created_at = chatMessage.getCreated_at();
    }
}
