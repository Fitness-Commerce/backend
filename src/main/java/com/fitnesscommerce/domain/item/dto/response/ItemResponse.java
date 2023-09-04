package com.fitnesscommerce.domain.item.dto.response;

import com.fitnesscommerce.domain.item.domain.ItemStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ItemResponse {

    private Long id;
    private Long memberId;
    private Long itemCategoryId; //itemCategoryTitle?
    private String itemName;
    private String itemDetail;
    private int itemPrice;
    private ItemStatus itemStatus;
    private Long buyerId;
    private List<String> itemImagesUrl;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public ItemResponse(Long id, Long memberId, Long itemCategoryId, String itemName,
                        String itemDetail, int itemPrice, ItemStatus itemStatus, Long buyerId,
                        List<String> itemImagesUrl, int viewCount,
                        LocalDateTime created_at,LocalDateTime updated_at) {
        this.id = id;
        this.memberId = memberId;
        this.itemCategoryId = itemCategoryId;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.buyerId = buyerId;
        this.itemImagesUrl = itemImagesUrl;
        this.viewCount = viewCount;
        this.createdAt = created_at;
        this.updatedAt = updated_at;
    }
}

