package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCategoryCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryUpdate;
import com.fitnesscommerce.domain.item.dto.request.ItemSortFilter;
import com.fitnesscommerce.domain.item.dto.response.CustomItemPageResponse;
import com.fitnesscommerce.domain.item.dto.response.IdResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemCategoryResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ItemCategoryApiController {

    private final ItemCategoryService itemCategoryService;

    @PostMapping("/api/categories")
    public ResponseEntity<IdResponse> create(@RequestBody ItemCategoryCreate request) {

        IdResponse response = itemCategoryService.createCategory(request);

        return ResponseEntity.created(URI.create("/api/category")).body(response);
    }

    @PutMapping("/api/categories/{categoryId}")
    public ResponseEntity<IdResponse> update(@RequestBody ItemCategoryUpdate request, @PathVariable Long categoryId){

        IdResponse response = itemCategoryService.updateCategory(request,categoryId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/categories/{categoryId}")
    public void delete(@PathVariable Long categoryId) {itemCategoryService.deleteCategory(categoryId);}

    @GetMapping("/api/categories")
    public ResponseEntity<List<ItemCategoryResponse>> getAllCategories() {
        List<ItemCategoryResponse> categories = itemCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/api/categories/{categoryId}/items")
    public ResponseEntity<CustomItemPageResponse> getItemsByCategoryPaging(
            @PathVariable Long categoryId,
            @ModelAttribute ItemSortFilter itemSortFilter) {

        return ResponseEntity.ok(itemCategoryService.getItemsByCategoryPaging(categoryId, itemSortFilter));
    }
}