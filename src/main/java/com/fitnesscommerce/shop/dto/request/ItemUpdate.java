package com.fitnesscommerce.shop.dto.request;

import com.fitnesscommerce.shop.domain.ItemImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class ItemUpdate {
    private Long itemCategoryId;
    private String itemName;
    private String itemDetail;
    private int itemPrice;
    private String itemStatus;
    private List<MultipartFile> images;

    @Builder
    public ItemUpdate(Long itemCategoryId, String itemName, String itemDetail,
                      int itemPrice, String itemStatus, List<MultipartFile> images) {
        this.itemCategoryId = itemCategoryId;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.images = images;
    }
}
