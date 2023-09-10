package com.fitnesscommerce.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostCommentCreate {
    private String content;

    @Builder
    public PostCommentCreate(String content){
        this.content = content;
    }
}
