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
public class PostCreate { // POST 요청에서 클라이언트로부터 전달되는 데이터를 담는 DTO입니다.

    private String postCategoryTitle;
    private String title;
    private String content;
    private List<MultipartFile> images; // 이미지 파일들을 따로 받기 위한 필드 추가


    @Builder
    public PostCreate(List<MultipartFile> images, String postCategoryTitle, String title, String content){
        this.images = images;
        this.postCategoryTitle = postCategoryTitle;
        this.title = title;
        this.content = content;

    }

}
