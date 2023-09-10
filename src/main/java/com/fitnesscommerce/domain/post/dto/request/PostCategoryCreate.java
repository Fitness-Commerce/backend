package com.fitnesscommerce.domain.post.dto.request;

import com.fitnesscommerce.domain.post.domain.PostCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostCategoryCreate {
    private String title;

    @Builder
    public PostCategoryCreate(String title){
        this.title = title;
    }
}
