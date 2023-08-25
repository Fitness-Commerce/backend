package com.fitnesscommerce.post.dto.request;

import com.fitnesscommerce.post.domain.PostImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdate {
    private Long postCategoryId;
    private List<PostImage> postImages;
    private String title;
    private String content;

    @Builder
    public PostUpdate(Long postCategoryId, List<PostImage> postImages, String title, String content){
        this.postCategoryId = postCategoryId;
        this.postImages = postImages;
        this.title = title;
        this.content = content;
    }
}
