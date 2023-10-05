package com.fitnesscommerce.domain.chat.controller;

import com.fitnesscommerce.domain.chat.dto.request.FirstMessageCreate;
import com.fitnesscommerce.domain.chat.dto.request.MessageCreate;
import com.fitnesscommerce.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/enter") // 메시지를 보낼 소켓 url
    public void enter(@Header("Authorization") String accessToken, //메시지를 보내는 사람
                      FirstMessageCreate request) { //판매자, 메시지 내용

        String roomName = chatService.createChatRoom(request, accessToken);

        //roomName, buyer, seller, time, content

        messagingTemplate.convertAndSend("/sub/chat/room/" + roomName, request);
        //메시지를 읽을 수 있는 웹소켓 url
    }

    @MessageMapping("/chat/message") // 메시지를 보낼 소켓 url
    public void after(@Header("Authorization") String accessToken, //메시지를 보내는 사람
                      MessageCreate request) { //판매자, 메시지 내용

        //todo 채팅방에 진입했을 때 서버에서 들어온 사람이 누구인지 알려주어야함
        String roomName = chatService.sendMessage(request, accessToken);

        //roomName, buyer, seller, time, content

        messagingTemplate.convertAndSend("/sub/chat/room/" + roomName, request);
        //메시지를 읽을 수 있는 웹소켓 url
    }


}
