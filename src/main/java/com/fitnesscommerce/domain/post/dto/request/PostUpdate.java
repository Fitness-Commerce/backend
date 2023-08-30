package com.fitnesscommerce.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdate {
    private String postCategoryTitle;
    private String title;
    private String content;
    private List<MultipartFile> images;

    @Builder
    public PostUpdate(String postCategoryTitle, String title, String content, List<MultipartFile> images){
        this.postCategoryTitle = postCategoryTitle;
        this.title = title;
        this.content = content;
        this.images = images;
    }
}
