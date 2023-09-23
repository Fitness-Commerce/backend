package com.fitnesscommerce.domain.item.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IdResponse {
    private Long id;

    @Builder
    public IdResponse(Long id){
        this.id = id;
    }
}
