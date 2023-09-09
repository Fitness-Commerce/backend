package com.fitnesscommerce.domain.member.dto.response;

import com.fitnesscommerce.domain.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberPostsResponse {

    private Long postId;
    private String categoryTitle;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @Builder
    public MemberPostsResponse(Long postId, String categoryTitle, String title, String content, int viewCount, LocalDateTime created_at, LocalDateTime updated_at) {
        this.postId = postId;
        this.categoryTitle = categoryTitle;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public MemberPostsResponse(Post post) {
        this.postId = post.getId();
        this.categoryTitle = post.getPostCategory().getTitle();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.created_at = post.getCreated_at();
        this.updated_at = post.getUpdated_at();
    }
}
