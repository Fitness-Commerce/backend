package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemStatusUpdate;
import com.fitnesscommerce.domain.item.dto.request.ItemUpdate;
import com.fitnesscommerce.domain.item.dto.response.CustomItemPageResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.service.ItemService;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/api/items")
    public ResponseEntity<Map<String,Long>> saveItem(@ModelAttribute ItemCreate itemCreate,
                                        MemberSession session) throws IOException {
        return ResponseEntity.ok(itemService.save(itemCreate, session));
    }

    @GetMapping("/api/items/{itemId}")
    public ItemResponse getItemById(@PathVariable Long itemId) {
        itemService.updateViewCount(itemId);

        return itemService.getItemResponseById(itemId);
    }

    @GetMapping("/api/items")
    public ResponseEntity<CustomItemPageResponse> getAllItemPaging(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) {

        return ResponseEntity.ok(itemService.getAllItemPaging(page, size, accessToken, search, orderBy, direction));
    }

    @PutMapping("/api/items/{itemId}")
    public ResponseEntity<Map<String,Long>> updateItem(@PathVariable Long itemId,
                                     @ModelAttribute ItemUpdate itemUpdate,
                                     MemberSession session) throws IOException {

        return ResponseEntity.ok(itemService.updateItem(itemId,itemUpdate,session));
    }

    @DeleteMapping("/api/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable Long itemId,MemberSession session) throws IOException {
        itemService.delete(itemId,session);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/items/updateStatus")
    public void updateItem(@RequestBody ItemStatusUpdate itemStatusUpdate,
                           MemberSession session) throws IOException {

        itemService.updateItemStatus(itemStatusUpdate,session);
    }

}