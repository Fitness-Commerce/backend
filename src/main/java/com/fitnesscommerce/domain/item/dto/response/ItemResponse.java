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
    private String itemCategoryTitle;
    private String itemName;
    private String itemDetail;
    private int itemPrice;
    private ItemStatus itemStatus;
    private Long buyerId;
    private List<String> itemImagesUrl;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //작성자 닉네임
    private String nickName;

    //거래가능지역
    private List<String> transactionArea;

    @Builder
    public ItemResponse(Long id, Long memberId, String itemCategoryTitle, String itemName,
                        String itemDetail, int itemPrice, ItemStatus itemStatus, Long buyerId,
                        List<String> itemImagesUrl, int viewCount,
                        LocalDateTime created_at,LocalDateTime updated_at, String nickName, List<String> transactionArea) {
        this.id = id;
        this.memberId = memberId;
        this.itemCategoryTitle = itemCategoryTitle;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.itemPrice = itemPrice;
        this.itemStatus = itemStatus;
        this.buyerId = buyerId;
        this.itemImagesUrl = itemImagesUrl;
        this.viewCount = viewCount;
        this.createdAt = created_at;
        this.updatedAt = updated_at;
        this.nickName = nickName;
        this.transactionArea = transactionArea;
    }
}

