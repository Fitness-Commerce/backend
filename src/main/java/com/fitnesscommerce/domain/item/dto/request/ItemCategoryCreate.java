package com.fitnesscommerce.domain.item.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemCategoryCreate {

    private String title;

    @Builder
    public ItemCategoryCreate(String title){
        this.title = title;
    }
}
