package com.fitnesscommerce.shop.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item_image")
@Getter
@NoArgsConstructor
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String url;

    @Builder
    public ItemImage(String fileName, String url, Item item) {
        this.fileName = fileName;
        this.url = url;
    }
}
