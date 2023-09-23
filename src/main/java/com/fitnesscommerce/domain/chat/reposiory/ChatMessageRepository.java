package com.fitnesscommerce.domain.chat.reposiory;

import com.fitnesscommerce.domain.chat.domain.ChatMessage;
import com.fitnesscommerce.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}
