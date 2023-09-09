package com.fitnesscommerce.domain.item.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemCommentUpdate {

    String content;

    @Builder
    public ItemCommentUpdate(String content){
        this.content = content;
    }
}
