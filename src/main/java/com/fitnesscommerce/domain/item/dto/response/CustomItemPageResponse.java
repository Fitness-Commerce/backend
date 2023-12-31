package com.fitnesscommerce.domain.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomItemPageResponse {

    private int totalPages;
    private List<ItemResponse> content;
}
