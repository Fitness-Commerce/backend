package com.fitnesscommerce.domain.item.dto.request;

import com.fitnesscommerce.domain.item.domain.ItemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemStatusUpdate {
    private Long itemId;
    private ItemStatus itemStatus;
    private Long buyerId;
}
