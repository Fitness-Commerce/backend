package com.fitnesscommerce.domain.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostSortFilter {
    private int page;
    private int size;
    private String order;

    @Builder
    public PostSortFilter(int page, int size, String order){
        this.page=page;
        this.size=size;
        this.order=order;
    }
}
