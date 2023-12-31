package com.fitnesscommerce.domain.chat.dto.response;

import com.fitnesscommerce.domain.chat.domain.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomsResponse {

    private Long roomId;
    private String roomName;
    private Long itemId;
    private String itemName;
    private Long buyerId;
    private String opponentNickname;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public ChatRoomsResponse(ChatRoom chatRoom, String opponentNickname, String lastMessage, LocalDateTime lastMessageTime) {
        this.roomId =  chatRoom.getId();
        this.roomName = chatRoom.getRoomName();
        this.itemId = chatRoom.getItem().getId();
        this.itemName = chatRoom.getItem().getItemName();
        this.buyerId = chatRoom.getBuyer().getId();
        this.opponentNickname = opponentNickname;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }
}
