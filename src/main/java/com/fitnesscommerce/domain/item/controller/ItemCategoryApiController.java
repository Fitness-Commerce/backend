package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCategoryCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryUpdate;
import com.fitnesscommerce.domain.item.dto.response.CustomItemPageResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemCategoryResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ItemCategoryApiController {

    private final ItemCategoryService itemCategoryService;


    @PostMapping("/api/categories")  //문제없음 = map -> dto 응답 형식(고려해볼만함) ResponseEntity -> 응답코드 201 생성
    public Map<String, Long> create(@RequestBody ItemCategoryCreate request) {return itemCategoryService.createCategory(request);}

    @PutMapping("/api/categories/{categoryId}") //문제없음
    public Map<String, Long> update(@RequestBody ItemCategoryUpdate request, @PathVariable Long categoryId){
        return itemCategoryService.updateCategory(request,categoryId);
    }

    @DeleteMapping("/api/categories/{categoryId}") //문제없음
    public void delete(@PathVariable Long categoryId) {itemCategoryService.deleteCategory(categoryId);}

    @GetMapping("/api/categories")
    public ResponseEntity<List<ItemCategoryResponse>> getAllCategories() {
        List<ItemCategoryResponse> categories = itemCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/api/categories/{categoryId}/items")
    public ResponseEntity<CustomItemPageResponse> getItemsByCategoryPaging(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") int page, //dto로 한방에 묶을 수 있나?
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        return ResponseEntity.ok(itemCategoryService.getItemsByCategoryPaging(categoryId, page, size, orderBy, direction));
        //필드로 빼서 필드명을 올림
    }
}