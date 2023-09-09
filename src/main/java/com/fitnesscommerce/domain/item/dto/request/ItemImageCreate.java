package com.fitnesscommerce.domain.item.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemImageCreate {
    private Long id;
    private String fileName;
    private String url;

    @Builder
    public ItemImageCreate(Long id, String fileName, String url){
        this.id = id;
        this.fileName = fileName;
        this.url = url;
    }
}
