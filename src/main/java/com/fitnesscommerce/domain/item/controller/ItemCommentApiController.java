package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCommentCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCommentUpdate;
import com.fitnesscommerce.domain.item.dto.response.CustomItemCommentPageResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemCommentResponse;
import com.fitnesscommerce.domain.item.service.ItemCommentService;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ItemCommentApiController {

    private final ItemCommentService itemCommentService;

    @PostMapping("/api/items/{itemId}/comments")
    public ResponseEntity<Long> createComment(@RequestBody ItemCommentCreate request,
                                              @PathVariable Long itemId,MemberSession session) {

        return ResponseEntity.ok(itemCommentService.createComment(request, session, itemId));
    }

    @PutMapping("/api/items/{itemId}/comments/{commentId}")
    public ResponseEntity<Long> updateComment(@RequestBody ItemCommentUpdate request,
                                              @PathVariable Long itemId,
                                              @PathVariable Long commentId,MemberSession session) {
        return ResponseEntity.ok(itemCommentService.updateComment(request, itemId, commentId,session));
    }

    @DeleteMapping("/api/items/{itemId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long itemId,
                                              @PathVariable Long commentId,MemberSession session) {
        itemCommentService.deleteComment(commentId, itemId, session);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/items/{itemId}/comments")
    public ResponseEntity<CustomItemCommentPageResponse> getCommentsByItem(@PathVariable Long itemId,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int size,
                                                                           @RequestParam(defaultValue = "id") String orderBy,
                                                                           @RequestParam(defaultValue = "DESC") String direction) {

        return ResponseEntity.ok(itemCommentService.getCommentsByItem(itemId, page, size, orderBy, direction));
    }
}
