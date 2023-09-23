package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCategoryCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryUpdate;
import com.fitnesscommerce.domain.item.dto.request.ItemSortFilter;
import com.fitnesscommerce.domain.item.dto.response.CustomItemPageResponse;
import com.fitnesscommerce.domain.item.dto.response.IdResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemCategoryResponse;
import com.fitnesscommerce.domain.item.service.ItemCategoryService;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품 카테고리", description = "상품 카테고리 관련 API")
public class ItemCategoryApiController {

    private final ItemCategoryService itemCategoryService;

    @Operation(summary = "카테고리 생성", description = "카테고리 생성 API")
    @ApiResponse(responseCode = "201", description = "카테고리 생성 성공")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @PostMapping("/api/categories")
    public ResponseEntity<IdResponse> create(@RequestBody ItemCategoryCreate request,
                                             @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {

        IdResponse response = itemCategoryService.createCategory(request,session);

        return ResponseEntity.created(URI.create("/api/category")).body(response);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 수정 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @ApiResponse(responseCode = "404", description = "카테고리 id를 찾을 수 없음")
    @PutMapping("/api/categories/{categoryId}")
    public ResponseEntity<IdResponse> update(
            @RequestBody ItemCategoryUpdate request,
            @Parameter(name = "categoryId", description = "카테고리 id", in = ParameterIn.PATH) @PathVariable Long categoryId,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {

        IdResponse response = itemCategoryService.updateCategory(request, categoryId, session);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @ApiResponse(responseCode = "404", description = "카테고리 id를 찾을 수 없음")
    @DeleteMapping("/api/categories/{categoryId}")
    public void delete(
            @Parameter(name = "categoryId", description = "카테고리 id", in = ParameterIn.PATH) @PathVariable Long categoryId,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {
        itemCategoryService.deleteCategory(categoryId, session);
    }

    @Operation(summary = "전체 카테고리 조회", description = "전체 카테고리 조회 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/api/categories")
    public ResponseEntity<List<ItemCategoryResponse>> getAllCategories() {
        List<ItemCategoryResponse> categories = itemCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "카테고리별 상품 전체 조회", description = "카테고리별 상품 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "카테고리 id를 찾을 수 없음")
    @GetMapping("/api/categories/{categoryId}/items")
    public ResponseEntity<CustomItemPageResponse> getItemsByCategoryPaging(
            @Parameter(name = "categoryId", description = "카테고리 id", in = ParameterIn.PATH) @PathVariable Long categoryId,
            @Parameter(name = "page&size&order", description = "요청할 page&size$order", in = ParameterIn.QUERY) @ModelAttribute ItemSortFilter itemSortFilter) {

        return ResponseEntity.ok(itemCategoryService.getItemsByCategoryPaging(categoryId, itemSortFilter));
    }
}
