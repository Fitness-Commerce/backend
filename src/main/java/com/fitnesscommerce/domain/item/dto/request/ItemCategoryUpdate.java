package com.fitnesscommerce.domain.item.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemCategoryUpdate {

    private String title;

    @Builder
    public ItemCategoryUpdate(String title){
        this.title = title;
    }
}
