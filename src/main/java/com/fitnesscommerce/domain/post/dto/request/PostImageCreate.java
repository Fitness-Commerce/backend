package com.fitnesscommerce.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostImageCreate {
    private Long id;
    private String fileName;
    private String url;

    @Builder
    public PostImageCreate(Long id, String fileName, String url){
        this.id = id;
        this.fileName = fileName;
        this.url = url;
    }
}
