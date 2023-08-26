package com.fitnesscommerce.shop.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_category")
@Getter
@NoArgsConstructor
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String title;

    private LocalDateTime createdAt;

    @Builder
    public ItemCategory(Long id, Item item, String title, LocalDateTime createdAt) {
        this.id = id;
        this.item = item;
        this.title = title;
        this.createdAt = createdAt;
    }
}
