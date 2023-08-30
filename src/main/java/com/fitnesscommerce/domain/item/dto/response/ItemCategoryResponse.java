package com.fitnesscommerce.domain.item.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ItemCategoryResponse {

    private Long id;
    private String title;
    private List<Long> item_ids;
    private LocalDateTime create_at;
    private LocalDateTime update_at;

    @Builder
    public ItemCategoryResponse(Long id, String title,List<Long> item_ids, LocalDateTime create_at, LocalDateTime update_at){
        this.id = id;
        this.title = title;
        this.item_ids = item_ids;
        this.create_at = create_at;
        this.update_at = update_at;
    }
}
