package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCategoryCreate;
import com.fitnesscommerce.domain.item.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemCategoryApiController {

    private final ItemCategoryService itemCategoryService;

    @PostMapping("/api/item/categories")
    public void create(@RequestBody ItemCategoryCreate request) {
        itemCategoryService.createCategory(request);
    }
}