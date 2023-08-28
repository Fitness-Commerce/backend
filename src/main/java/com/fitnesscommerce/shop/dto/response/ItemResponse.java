package com.fitnesscommerce.shop.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ItemResponse {

    private Long id;
    private Long memberId;
    private Long itemCategoryId;
    private String itemName;
    private String itemDetail;
    private int itemPrice;
    private String itemStatus;
    private List<String> itemImagesUrl;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ItemResponse(Long id, Long memberId, Long itemCategoryId, String itemName,
                        String itemDetail, int itemPrice, String itemStatus,
                        List<String> itemImagesUrl, int viewCount,
                        LocalDateTime createdAt,LocalDateTime updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.itemCategoryId = itemCategoryId;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.itemImagesUrl = itemImagesUrl;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
