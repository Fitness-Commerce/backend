package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCommentCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCommentUpdate;
import com.fitnesscommerce.domain.item.dto.request.ItemSortFilter;
import com.fitnesscommerce.domain.item.dto.response.CustomItemCommentPageResponse;
import com.fitnesscommerce.domain.item.dto.response.IdResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemCommentResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.service.ItemCommentService;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ItemCommentApiController {

    private final ItemCommentService itemCommentService;

    @PostMapping("/api/items/{itemId}/comments")
    public ResponseEntity<IdResponse> createComment(@RequestBody ItemCommentCreate request,
                                                    @PathVariable Long itemId, MemberSession session) {

        IdResponse response = itemCommentService.createComment(request, session, itemId);

        return ResponseEntity.created(URI.create("/api/items/comments")).body(response);
    }

    @PutMapping("/api/items/{itemId}/comments/{commentId}")
    public ResponseEntity<IdResponse> updateComment(@RequestBody ItemCommentUpdate request,
                                              @PathVariable Long itemId,
                                              @PathVariable Long commentId,MemberSession session) {

        IdResponse response = itemCommentService.updateComment(request, itemId, commentId,session);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/items/{itemId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long itemId,
                                              @PathVariable Long commentId,MemberSession session) {
        itemCommentService.deleteComment(commentId, itemId, session);
    }

    @GetMapping("/api/items/{itemId}/comments")
    public ResponseEntity<CustomItemCommentPageResponse> getCommentsByItem(@PathVariable Long itemId,
                                                                           @ModelAttribute ItemSortFilter itemSortFilter) {

        return ResponseEntity.ok(itemCommentService.getCommentsByItem(itemId, itemSortFilter));
    }
}
