package com.fitnesscommerce.domain.item.domain;

import com.fitnesscommerce.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_comment")
@Getter
@NoArgsConstructor
public class ItemComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String content;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Builder
    public ItemComment(Member member, Item item, String content) {
        this.member = member;
        this.item = item;
        this.content = content;
        this.created_at = LocalDateTime.now();
    }

    public void updateComment(String content){
        this.content = content;
    }
}
