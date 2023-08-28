package com.fitnesscommerce.shop.controller;

import com.fitnesscommerce.shop.domain.Item;
import com.fitnesscommerce.shop.dto.request.ItemCreate;
import com.fitnesscommerce.shop.dto.request.ItemUpdate;
import com.fitnesscommerce.shop.dto.response.ItemResponse;
import com.fitnesscommerce.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping(produces = "application/json;charset=UTF-8", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> saveItem(
            @RequestParam Long memberId,
            @RequestParam Long itemCategoryId,
            @RequestParam String itemName,
            @RequestParam String itemDetail,
            @RequestParam Integer itemPrice,
            @RequestParam String itemStatus,
            @RequestPart(required = false) List<MultipartFile> images
    ) {
        ItemCreate itemCreate = ItemCreate.builder()
                .memberId(memberId)
                .itemCategoryId(itemCategoryId)
                .itemName(itemName)
                .itemDetail(itemDetail)
                .itemPrice(itemPrice)
                .itemStatus(itemStatus)
                .images(images)
                .build();

        Item savedItem = itemService.saveItem(itemCreate);

        if (savedItem != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("상품등록 완료!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품등록 실패!");
        }
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long itemId) {
        ItemResponse itemResponse = itemService.getItemResponseById(itemId);
        return ResponseEntity.ok(itemResponse);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> itemResponses = itemService.getAllItemResponses();
        return ResponseEntity.ok(itemResponses);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long itemId,
            @RequestParam("itemCategoryId") Long itemCategoryId,
            @RequestParam("itemName") String itemName,
            @RequestParam("itemDetail") String itemDetail,
            @RequestParam("itemPrice") int itemPrice,
            @RequestParam("itemStatus") String itemStatus,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        ItemUpdate itemUpdate = ItemUpdate.builder()
                .itemCategoryId(itemCategoryId)
                .itemName(itemName)
                .itemDetail(itemDetail)
                .itemPrice(itemPrice)
                .itemStatus(itemStatus)
                .images(images)
                .build();

        Item updatedItem = itemService.updateItem(itemId, itemUpdate);

        ItemResponse response = itemService.getItemResponseById(updatedItem.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok("상품 삭제 완료!");
    }


}