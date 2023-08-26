package com.fitnesscommerce.shop.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class ItemCreate {

    private Long memberId;
    private Long itemCategoryId;
    private String itemName;
    private String itemDetail;
    private Integer itemPrice;
    private Integer itemStatus;

    //이미지 파일들
    private List<MultipartFile> images;

    @Builder
    public ItemCreate(Long memberId, List<MultipartFile> images, Long itemCategoryId,
                             String itemName, String itemDetail, Integer itemPrice) {
        this.memberId = memberId;
        this.images = images;
        this.itemCategoryId = itemCategoryId;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = 0;
    }

}
