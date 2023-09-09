package com.fitnesscommerce.domain.member.dto.response;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemImage;
import com.fitnesscommerce.domain.item.domain.ItemStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberSalesResponse {

    private Long itemId;
    private String categoryName; //itemCategoryTitle?
    private String itemName;
    private String itemDetail;
    private int itemPrice;
    private ItemStatus itemStatus;
    private String mainImageUrl;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public MemberSalesResponse(Long itemId, String categoryName, String itemName,
                               String itemDetail, int itemPrice, ItemStatus itemStatus,
                               String mainImageUrl, int viewCount, LocalDateTime createdAt,
                               LocalDateTime updatedAt) {
        this.itemId = itemId;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.mainImageUrl = mainImageUrl;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MemberSalesResponse(Item item, String mainImageUrl) {
        this.itemId = item.getId();
        this.categoryName = item.getItemCategory().getTitle();
        this.itemName = item.getItemName();
        this.itemDetail = item.getItemDetail();
        this.itemPrice = item.getItemPrice();
        this.itemStatus = item.getItemStatus();
        this.mainImageUrl = mainImageUrl;
        this.viewCount = item.getViewCount();
        this.createdAt = item.getCreated_at();
        this.updatedAt = item.getUpdated_at();
    }
}
