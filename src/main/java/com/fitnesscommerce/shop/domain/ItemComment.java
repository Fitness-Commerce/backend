package com.fitnesscommerce.shop.domain;

import com.fitnesscommerce.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_comment")
@Getter
@NoArgsConstructor
public class ItemComment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String content;

    @Builder
    public ItemComment(Member member, Item item, String content) {
        this.member = member;
        this.item = item;
        this.content = content;
    }
}
