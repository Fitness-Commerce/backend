package com.fitnesscommerce.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostCategoryResponse {
    private Long id;
    private String title;
    private List<Long> post_ids;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PostCategoryResponse(Long id, String title, List<Long> post_ids, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.title = title;
        this.post_ids = post_ids;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
