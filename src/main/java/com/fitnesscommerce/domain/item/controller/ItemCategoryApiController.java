package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCategoryCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryUpdate;
import com.fitnesscommerce.domain.item.dto.response.ItemCategoryResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemCategoryApiController {

    private final ItemCategoryService itemCategoryService;

    @PostMapping("/api/categories")
    public Long create(@RequestBody ItemCategoryCreate request) {return itemCategoryService.createCategory(request);}

    @PutMapping("/api/categories/{categoryId}")
    public Long update(@RequestBody ItemCategoryUpdate request, @PathVariable Long categoryId){
        return itemCategoryService.updateCategory(request,categoryId);
    }

    @DeleteMapping("/api/categories/{categoryId}")
    public void delete(@PathVariable Long categoryId) {itemCategoryService.deleteCategory(categoryId);}

    @GetMapping("/api/categories")
    public ResponseEntity<List<ItemCategoryResponse>> getAllCategories() {
        List<ItemCategoryResponse> categories = itemCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/api/category/{categoryId}/items")
    public Page<ItemResponse> getItemsByCategoryPaging(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return itemCategoryService.getItemsByCategoryPaging(categoryId, page, size);
    }
}