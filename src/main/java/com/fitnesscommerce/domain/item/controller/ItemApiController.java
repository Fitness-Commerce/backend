package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemUpdate;
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

@RestController
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/api/items")
    public ResponseEntity<Long> saveItem(@ModelAttribute ItemCreate itemCreate,
                                           MemberSession session) throws IOException {
        return ResponseEntity.ok(itemService.save(itemCreate, session));
    }

    @GetMapping("/api/items/{itemId}")
    public ItemResponse getItemById(@PathVariable Long itemId) {
        itemService.updateViewCount(itemId);

        return itemService.getItemResponseById(itemId);
    }

    @GetMapping("/api/items")
    public ResponseEntity<Page<ItemResponse>> getAllItemsWithPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ItemResponse> itemResponsesPage = itemService.getAllItemPaging(page, size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Previous-Page", itemResponsesPage.hasPrevious() ? itemResponsesPage.previousPageable().getPageNumber() + "" : "");
        headers.add("Next-Page", itemResponsesPage.hasNext() ? itemResponsesPage.nextPageable().getPageNumber() + "" : "");

        return ResponseEntity.ok().headers(headers).body(itemResponsesPage);
    }

    @PutMapping("/api/items/{itemId}")
    public ResponseEntity<Long> updateItem(@PathVariable Long itemId,
                                     @ModelAttribute ItemUpdate itemUpdate,
                                     MemberSession session) throws IOException {

        return ResponseEntity.ok(itemService.updateItem(itemId,itemUpdate));
    }

    @DeleteMapping("/api/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable Long itemId) throws IOException {
        itemService.delete(itemId);

        return ResponseEntity.ok().build();
    }


}