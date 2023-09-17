package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemSortFilter;
import com.fitnesscommerce.domain.item.dto.request.ItemStatusUpdate;
import com.fitnesscommerce.domain.item.dto.request.ItemUpdate;
import com.fitnesscommerce.domain.item.dto.response.CustomItemPageResponse;
import com.fitnesscommerce.domain.item.dto.response.IdResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.service.ItemService;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품 관련 API")
public class ItemApiController {

    private final ItemService itemService;

    @Operation(summary = "상품 등록", description = "상품 등록 API")
    @ApiResponse(responseCode = "201", description = "상품 등록 성공")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @ApiResponse(responseCode = "404", description = "카테고리 id를 찾을 수 없음")
    @PostMapping("/api/items")
    public ResponseEntity<IdResponse> saveItem(@ModelAttribute ItemCreate itemCreate,
                                               @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {

        IdResponse response = itemService.save(itemCreate, session);

        return ResponseEntity.created(URI.create("/api/items/" + response.getId())).body(response);
    }

    @Operation(summary = "상품 단건 조회", description = "상품 단건 조회 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "상품 id를 찾을 수 없음")
    @GetMapping("/api/items/{itemId}")
    public ItemResponse getItemById(
            @Parameter(name = "itemId", description = "상품 id", in = ParameterIn.PATH) @PathVariable Long itemId) {
        itemService.updateViewCount(itemId);

        return itemService.getItemResponseById(itemId);
    }

    @Operation(summary = "상품 전체 조회", description = "상품 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @GetMapping("/api/items")
    public ResponseEntity<CustomItemPageResponse> getAllItemPaging(
            @Parameter(name = "Authorization", description = "지역분류할 Authorization", in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = false) String accessToken,
            @Parameter(name = "page&size&order", description = "요청할 page&size$order", in = ParameterIn.QUERY) @ModelAttribute ItemSortFilter itemSortFilter,
            @Parameter(name = "search", description = "검색요청할 search", in = ParameterIn.QUERY) @RequestParam(value = "search", required = false) String search
    ) {

        CustomItemPageResponse response = itemService.getAllItemPaging(itemSortFilter, accessToken, search);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 수정", description = "상품 수정 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @PutMapping("/api/items/{itemId}")
    public ResponseEntity<IdResponse> updateItem(
            @Parameter(name = "itemId", description = "상품 id", in = ParameterIn.PATH) @PathVariable Long itemId,
            @ModelAttribute ItemUpdate itemUpdate,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {

        IdResponse response = itemService.updateItem(itemId, itemUpdate, session);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 삭제", description = "상품 삭제 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @DeleteMapping("/api/items/{itemId}")
    public void deleteItem(
            @Parameter(name = "itemId", description = "상품 id", in = ParameterIn.PATH) @PathVariable Long itemId,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {
        itemService.delete(itemId, session);
    }

    @Operation(summary = "상품 상태 업데이트", description = "상품 상태 업데이트 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @PutMapping("/api/items/updateStatus")
    public void updateItemStatus(@RequestBody ItemStatusUpdate itemStatusUpdate,
                                 @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {

        itemService.updateItemStatus(itemStatusUpdate, session);
    }
}
