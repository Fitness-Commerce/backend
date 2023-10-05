package com.fitnesscommerce.domain.item.controller;

import com.fitnesscommerce.domain.item.dto.request.ItemCommentCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCommentUpdate;
import com.fitnesscommerce.domain.item.dto.request.ItemSortFilter;
import com.fitnesscommerce.domain.item.dto.response.CustomItemCommentPageResponse;
import com.fitnesscommerce.domain.item.dto.response.IdResponse;
import com.fitnesscommerce.domain.item.service.ItemCommentService;
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

@RestController
@RequiredArgsConstructor
@Tag(name = "상품 댓글", description = "상품 댓글 관련 API")
public class ItemCommentApiController {

    private final ItemCommentService itemCommentService;

    @Operation(summary = "댓글 등록", description = "댓글 등록 API")
    @ApiResponse(responseCode = "201", description = "댓글 등록 성공")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @ApiResponse(responseCode = "404", description = "상품 id를 찾을 수 없음")
    @PostMapping("/api/items/{itemId}/comments")
    public ResponseEntity<IdResponse> createComment(
            @RequestBody ItemCommentCreate request,
            @Parameter(name = "itemId", description = "상품 id", in = ParameterIn.PATH) @PathVariable Long itemId,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {

        IdResponse response = itemCommentService.createComment(request, session, itemId);

        return ResponseEntity.created(URI.create("/api/items/comments")).body(response);
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @ApiResponse(responseCode = "404", description = "상품 댓글 id를 찾을 수 없음")
    @PutMapping("/api/items/{itemId}/comments/{commentId}")
    public ResponseEntity<IdResponse> updateComment(
            @RequestBody ItemCommentUpdate request,
            @Parameter(name = "itemId", description = "상품 id", in = ParameterIn.PATH) @PathVariable Long itemId,
            @Parameter(name = "commentId", description = "상품 댓글 id", in = ParameterIn.PATH) @PathVariable Long commentId,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {

        IdResponse response = itemCommentService.updateComment(request, itemId, commentId, session);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @ApiResponse(responseCode = "404", description = "상품 댓글 id를 찾을 수 없음")
    @DeleteMapping("/api/items/{itemId}/comments/{commentId}")
    public void deleteComment(
            @Parameter(name = "itemId", description = "상품 id", in = ParameterIn.PATH) @PathVariable Long itemId,
            @Parameter(name = "commentId", description = "상품 댓글 id", in = ParameterIn.PATH) @PathVariable Long commentId,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {
        itemCommentService.deleteComment(commentId, itemId, session);
    }

    @Operation(summary = "상품 댓글 전체 조회", description = "상품 댓글 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "상품 id를 찾을 수 없음")
    @GetMapping("/api/items/{itemId}/comments")
    public ResponseEntity<CustomItemCommentPageResponse> getCommentsByItem(
            @Parameter(name = "itemId", description = "상품 id", in = ParameterIn.PATH) @PathVariable Long itemId,
            @Parameter(name = "page&size&order", description = "요청할 page&size$order", in = ParameterIn.QUERY) @ModelAttribute ItemSortFilter itemSortFilter) {

        return ResponseEntity.ok(itemCommentService.getCommentsByItem(itemId, itemSortFilter));
    }
}
