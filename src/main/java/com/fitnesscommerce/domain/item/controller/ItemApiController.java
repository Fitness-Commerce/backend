package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemUpdate;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.service.ItemService;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/api/items")
    public ResponseEntity saveItem(@ModelAttribute ItemCreate itemCreate,
                                   MemberSession session) throws IOException {
        itemService.save(itemCreate, session);

        return ResponseEntity.created(URI.create("/api/items")).build();
    }

    @GetMapping("/api/items/{itemId}")
    public ItemResponse getItemById(@PathVariable Long itemId) {
        itemService.updateViewCount(itemId);
        ItemResponse response = itemService.getItemResponseById(itemId);

        return response;
    }

    @GetMapping("/api/items") //pagenation 추가 해주세요
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        List<ItemResponse> itemResponses = itemService.getAllItemResponses();
        return ResponseEntity.ok(itemResponses);
    }

    @PutMapping("/api/items/{itemId}")
    public ResponseEntity updateItem(@PathVariable Long itemId,
                                     @ModelAttribute ItemUpdate itemUpdate,
                                     MemberSession session) throws IOException {

        itemService.updateItem(itemId,itemUpdate);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable Long itemId) throws IOException {
        itemService.delete(itemId);

        return ResponseEntity.ok().build();
    }


}