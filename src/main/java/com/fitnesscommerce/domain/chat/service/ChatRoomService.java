package com.fitnesscommerce.domain.chat.service;

import com.fitnesscommerce.domain.chat.domain.ChatMessage;
import com.fitnesscommerce.domain.chat.domain.ChatRoom;
import com.fitnesscommerce.domain.chat.dto.response.ChatRoomsResponse;
import com.fitnesscommerce.domain.chat.dto.response.MessagesResponse;
import com.fitnesscommerce.domain.chat.reposiory.ChatMessageRepository;
import com.fitnesscommerce.domain.chat.reposiory.ChatRoomRepository;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    //채팅방 목록
    public List<ChatRoomsResponse> getList(MemberSession session) {

        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);
        return chatRoomRepository.findByBuyerOrSeller(member, member).stream()
                .map(chatRoom -> {
                    List<ChatMessage> messages = chatMessageRepository.findByChatRoom(chatRoom);
                    String lastMessage = messages.get(messages.size()-1).getMessage();
                    LocalDateTime lastMessageTime = messages.get(messages.size()-1).getCreated_at();
                    String opponentNickname = member.equals(chatRoom.getBuyer()) ? chatRoom.getSeller().getNickname() : chatRoom.getBuyer().getNickname();
                    return new ChatRoomsResponse(chatRoom, opponentNickname, lastMessage, lastMessageTime);
                })
                .collect(Collectors.toList());

    }

    // 특정 방 채팅내역
    public List<MessagesResponse> getMessages(MemberSession session, Long chatRoomId) {

        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(IllegalStateException::new);
        //todo buyer + itemId로 채팅방을 찾아야함

        return chatMessageRepository.findByChatRoom(chatRoom).stream()
                .map(chatMessage -> new MessagesResponse(chatMessage, member))
                .collect(Collectors.toList());

    }
}
