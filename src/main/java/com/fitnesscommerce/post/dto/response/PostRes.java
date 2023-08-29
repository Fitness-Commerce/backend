package com.fitnesscommerce.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostRes { //PostResponseDTO는 엔티티에 대한 정보를 클라이언트로 반환하기 위한 DTO입니다.
    private Long id;
    private Long memberId;
    private Long postCategoryId;
    private List<String> url;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Builder
    public PostRes(Long id, Long memberId, Long postCategoryId, List<String> url,
                   String title, String content, int viewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.postCategoryId = postCategoryId;
        this.url = url;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
