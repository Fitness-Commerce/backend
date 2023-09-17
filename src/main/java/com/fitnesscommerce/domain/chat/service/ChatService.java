package com.fitnesscommerce.domain.chat.service;

import com.fitnesscommerce.domain.chat.domain.ChatMessage;
import com.fitnesscommerce.domain.chat.domain.ChatRoom;
import com.fitnesscommerce.domain.chat.dto.request.FirstMessageCreate;
import com.fitnesscommerce.domain.chat.dto.request.MessageCreate;
import com.fitnesscommerce.domain.chat.reposiory.ChatMessageRepository;
import com.fitnesscommerce.domain.chat.reposiory.ChatRoomRepository;
import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.exception.ItemNotFound;
import com.fitnesscommerce.domain.item.repository.ItemRepository;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ItemRepository itemRepository;
    private final AppConfig appConfig;

    @Transactional
    public String createChatRoom(FirstMessageCreate request, String accessToken) {

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String token = accessToken.substring(7);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Long memberId = claims.getBody().get("memberId", Long.class);

        Item item = itemRepository.findById(request.getItemId()).orElseThrow(ItemNotFound::new);

        Member buyer = memberRepository.findById(memberId).orElseThrow(IdNotFound::new);
        Member seller = item.getMember();

        LocalDateTime now = LocalDateTime.now();

        request.setNickname(buyer.getNickname());
        request.setMessageTime(now);

        //채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(request.getRoomName())
                .buyer(buyer)
                .seller(seller)
                .created_at(now)
                .item(item)
                .build();

        //채팅방 저장
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //메시지 생성
        ChatMessage message = ChatMessage.builder()
                .message(request.getMessage())
                .created_at(now)
                .sender(buyer)
                .chatRoom(chatRoom)
                .build();

        System.out.println("여기까지 오냐?");

        //메시지 저장
        chatMessageRepository.save(message);

        return chatRoom.getRoomName();

    }

    @Transactional
    public String sendMessage(MessageCreate request, String accessToken) {
        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String token = accessToken.substring(7);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        Long memberId = claims.getBody().get("memberId", Long.class);

        Member sender = memberRepository.findById(memberId).orElseThrow(IdNotFound::new);
        request.setMemberName(sender.getNickname());

        Optional<ChatRoom> chatroom = chatRoomRepository.findByRoomName(request.getRoomName());

        LocalDateTime now = LocalDateTime.now();
        request.setMessageTime(now);

        //todo 1번이 1번 상품 채팅에 접근이 되면 안됌
        ChatMessage message = ChatMessage.builder()
                .message(request.getMessage())
                .created_at(now)
                .chatRoom(chatroom.get())
                .sender(sender)
                .build();

        chatMessageRepository.save(message);

        return chatroom.get().getRoomName();


    }
}
