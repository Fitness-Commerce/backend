package com.fitnesscommerce.shop.domain;

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
    private List<ItemImage> itemImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemCategory_id")
    private ItemCategory itemCategory;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemComment> comments = new ArrayList<>();

    private String itemName;

    private String itemDetail;

    private Integer itemPrice;

    private Integer itemStatus;

    @Builder
    public Item(Member member, List<ItemImage> itemImages, ItemCategory itemCategory,
                String itemName, String itemDetail, Integer itemPrice) {
        this.member = member;
        this.itemImages = itemImages;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = 0;
    }

    // Change method to update item properties and update updatedAt
    public Item change(Member member, List<ItemImage> itemImages, ItemCategory itemCategory,
                       String itemName, String itemDetail, Integer itemPrice, Integer itemStatus) {
        this.member = member;
        this.itemImages = itemImages;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.updatedAt = LocalDateTime.now();  // Update updatedAt
        return this;  // Return the updated item
    }
}
