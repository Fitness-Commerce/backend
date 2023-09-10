package com.fitnesscommerce.domain.item.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_category")
@Getter
@Setter
@NoArgsConstructor
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Builder
    public ItemCategory(String title) {
        this.title = title;
        this.created_at = LocalDateTime.now();
    }

    public void updateCategory(String title){
        this.title = title;
        this.updated_at = LocalDateTime.now();
    }
}
