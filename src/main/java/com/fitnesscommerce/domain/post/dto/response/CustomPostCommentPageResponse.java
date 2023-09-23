package com.fitnesscommerce.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPostCommentPageResponse {
    private int totalPages;
    private List<PostCommentResponse> content;
}
