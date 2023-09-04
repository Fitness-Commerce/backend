package com.fitnesscommerce.domain.item.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemCommentCreate {

    String content;

    @Builder
    public ItemCommentCreate(String content){
        this.content = content;
    }
}
