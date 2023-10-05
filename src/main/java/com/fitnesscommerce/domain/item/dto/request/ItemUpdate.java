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
public class ItemUpdate {
    private String categoryTitle;
    private String itemName;
    private String itemDetail;
    private int itemPrice;
    private List<MultipartFile> images;

    @Builder
    public ItemUpdate(String categoryTitle, String itemName, String itemDetail,
                      int itemPrice, List<MultipartFile> images) {
        this.categoryTitle = categoryTitle;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.images = images;
    }
}