package com.fitnesscommerce.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostCategoryUpdate {
    private String title;

    @Builder
    public PostCategoryUpdate(String title){
        this.title = title;
    }
}
