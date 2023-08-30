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
public class ItemCreate {

    private String categoryTitle;
    private String itemName;
    private String itemDetail;
    private Integer itemPrice;
    private List<MultipartFile> images;

    @Builder
    public ItemCreate(List<MultipartFile> images, String categoryTitle,
                      String itemName, String itemDetail, Integer itemPrice) {
        this.images = images;
        this.categoryTitle = categoryTitle;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
    }
}
