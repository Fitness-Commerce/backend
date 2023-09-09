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
    public ResponseEntity<IdResponse> saveItem(@ModelAttribute ItemCreate itemCreate,
                                               MemberSession session) throws IOException {

        IdResponse response = itemService.save(itemCreate, session);

        return ResponseEntity.created(URI.create("/api/items/" + response.getId())).body(response);
    }

    @GetMapping("/api/items/{itemId}")
    public ItemResponse getItemById(@PathVariable Long itemId) {
        itemService.updateViewCount(itemId);

        return itemService.getItemResponseById(itemId);
    }

    @GetMapping("/api/items")
    public ResponseEntity<CustomItemPageResponse> getAllItemPaging(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            @ModelAttribute ItemSortFilter itemSortFilter,
            @RequestParam(value = "search", required = false) String search
            ) {

        CustomItemPageResponse response = itemService.getAllItemPaging(itemSortFilter, accessToken, search);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/items/{itemId}")
    public ResponseEntity<IdResponse> updateItem(@PathVariable Long itemId,
                                     @ModelAttribute ItemUpdate itemUpdate,
                                     MemberSession session) throws IOException {

        IdResponse response = itemService.updateItem(itemId,itemUpdate,session);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/items/{itemId}")
    public void deleteItem(@PathVariable Long itemId,MemberSession session) throws IOException {
        itemService.delete(itemId,session);
    }

    @PutMapping("/api/items/updateStatus") //얘기해봐야함
    public void updateItem(@RequestBody ItemStatusUpdate itemStatusUpdate,
                           MemberSession session) throws IOException {

        itemService.updateItemStatus(itemStatusUpdate,session);
    }

}