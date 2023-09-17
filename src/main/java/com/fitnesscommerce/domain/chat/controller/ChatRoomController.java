package com.fitnesscommerce.domain.chat.controller;

import com.fitnesscommerce.domain.chat.dto.response.ChatRoomsResponse;
import com.fitnesscommerce.domain.chat.dto.response.MessagesResponse;
import com.fitnesscommerce.domain.chat.service.ChatRoomService;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/api/chatRooms") //채팅목록
    public List<ChatRoomsResponse> getAll(MemberSession session) {
        return chatRoomService.getList(session);
    }

    @GetMapping("/api/chatRoom/{chatRoomId}") //채팅방 - 메시지기록
    public List<MessagesResponse> getOne(MemberSession session,
                                         @PathVariable Long chatRoomId) {
        return chatRoomService.getMessages(session, chatRoomId);
    }
}
