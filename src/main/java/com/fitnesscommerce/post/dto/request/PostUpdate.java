package com.fitnesscommerce.post.dto.request;

import com.fitnesscommerce.post.domain.PostImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Getter
@NoArgsConstructor
public class PostUpdate {
    private Long postCategoryId;
    private String title;
    private String content;
    private List<MultipartFile> images;

    @Builder
    public PostUpdate(Long postCategoryId, String title, String content, List<MultipartFile> images){
        this.postCategoryId = postCategoryId;
        this.title = title;
        this.content = content;
        this.images = images;
    }
}
