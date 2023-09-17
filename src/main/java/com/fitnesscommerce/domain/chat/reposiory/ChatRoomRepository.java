package com.fitnesscommerce.domain.chat.reposiory;

import com.fitnesscommerce.domain.chat.domain.ChatRoom;
import com.fitnesscommerce.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomName(String roomName);

    List<ChatRoom> findByBuyerOrSeller(Member buyer, Member seller);

    //Optional<ChatRoom> findByBuyerAndSeller(Member buyer, Member seller);
}
