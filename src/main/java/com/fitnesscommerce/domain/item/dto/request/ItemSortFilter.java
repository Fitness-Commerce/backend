package com.fitnesscommerce.domain.item.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemSortFilter {
    private int page;
    private int size;
    private String order;

    @Builder
    public ItemSortFilter(int page, int size, String order) {
        this.page = page;
        this.size = size;
        this.order = order;
    }
}
