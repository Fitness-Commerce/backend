package com.fitnesscommerce.domain.item.domain;

import com.fitnesscommerce.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> itemImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;

//    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ItemComment> comments = new ArrayList<>();

    private String itemName;

    private String itemDetail;

    private Integer itemPrice;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    private Integer viewCount;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Builder
    public Item(Member member, ItemCategory itemCategory,
                String itemName, String itemDetail, Integer itemPrice, Integer viewCount) {
        this.member = member;
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = ItemStatus.SELLING;
        this.created_at = LocalDateTime.now();
        this.viewCount = 0;
    }

    public void update(ItemCategory itemCategory, String itemName, String itemDetail,
                       Integer itemPrice, ItemStatus itemStatus) {
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.updated_at = LocalDateTime.now();

    }

    public void addItemImage(ItemImage itemImage) {
        this.itemImages.add(itemImage);
    }
}
