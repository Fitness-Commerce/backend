package com.fitnesscommerce.post.dto.request;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostCreate { //PostRequestDTO는 POST 요청에서 클라이언트로부터 전달되는 데이터를 담는 DTO입니다.
    private Long postCategoryId;
    private Long memberId;
    private String title;
    private String content;
    private int viewCount;

    // 이미지 파일들을 따로 받기 위한 필드 추가
    private List<MultipartFile> images;


    @Builder
    public PostCreate(Long postCategoryId, Long memberId, String title, String content, int viewCount, List<MultipartFile> images){
        this.postCategoryId = postCategoryId;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.images = images;
    }



}
