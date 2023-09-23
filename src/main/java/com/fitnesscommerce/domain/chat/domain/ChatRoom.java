package com.fitnesscommerce.domain.chat.domain;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_room")
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime created_at;

    @Builder
    public ChatRoom(String roomName, Member buyer, Member seller, LocalDateTime created_at, Item item) {
        this.roomName = roomName;
        this.buyer = buyer;
        this.seller = seller;
        this.created_at = created_at;
        this.item = item;
    }

}
