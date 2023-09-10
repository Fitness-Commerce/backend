package com.fitnesscommerce.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostCommentUpdate {
    private String content;

    @Builder
    public PostCommentUpdate(String content){
        this.content = content;
    }
}
