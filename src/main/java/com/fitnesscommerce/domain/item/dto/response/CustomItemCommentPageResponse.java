package com.fitnesscommerce.domain.item.dto.response;

import com.fitnesscommerce.domain.item.domain.ItemComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomItemCommentPageResponse {

    private int totalPages;
    private List<ItemCommentResponse> content;
}
