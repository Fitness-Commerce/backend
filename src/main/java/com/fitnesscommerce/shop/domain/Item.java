package com.fitnesscommerce.shop.domain;

import com.fitnesscommerce.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor
public class Item extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<ItemImage> itemImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemCategory_id")
    private ItemCategory itemCategory;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemComment> comments = new ArrayList<>();

    private String itemName;

    private String itemDetail;

    private Integer itemPrice;

    private String itemStatus;

    private Integer viewCount;

    @Builder
    public Item(Member member, List<ItemImage> itemImages, ItemCategory itemCategory,
                String itemName, String itemDetail, Integer itemPrice, String itemStatus, Integer viewCount) {
        this.member = member;
        this.itemImages = (itemImages != null) ? itemImages : new ArrayList<>();
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.createdAt = LocalDateTime.now();
        this.viewCount = 0;
    }

    public void change(ItemCategory itemCategory, String itemName, String itemDetail,
                       Integer itemPrice, String itemStatus, List<ItemImage> newImages) {
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.updatedAt = LocalDateTime.now();

        if (newImages != null) {
            this.itemImages.addAll(newImages);
        }

    }
}
