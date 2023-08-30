package com.fitnesscommerce.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse { //PostResponseDTO는 엔티티에 대한 정보를 클라이언트로 반환하기 위한 DTO입니다.
    private Long id;
    private Long memberId;
    private Long postCategoryId;
    private String title;
    private String content;
    private List<String> postImageUrl;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Builder
    public PostResponse(Long id, Long memberId, Long postCategoryId, List<String> postImageUrl,
                        String title, String content, int viewCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.postCategoryId = postCategoryId;
        this.postImageUrl = postImageUrl;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
