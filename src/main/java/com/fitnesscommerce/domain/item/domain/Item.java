package com.fitnesscommerce.domain.item.domain;

import com.fitnesscommerce.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemCategory itemCategory;

    private String itemName;

    private String itemDetail;

    private Integer itemPrice;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    private Integer viewCount;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Builder
    public Item(Member member, ItemCategory itemCategory,
                String itemName, String itemDetail, Integer itemPrice) {
        this.member = member;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = ItemStatus.SELLING;
        this.buyer = null;
        this.created_at = LocalDateTime.now();
        this.viewCount = 0;
    }

    public void update(ItemCategory itemCategory, String itemName, String itemDetail,
                       Integer itemPrice) {
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.updated_at = LocalDateTime.now();
    }

    public void updateItemStatus(Long id, ItemStatus itemStatus, Member buyer){
        this.id = id;
        this.itemStatus = itemStatus;
        this.buyer = buyer;
    }

}
