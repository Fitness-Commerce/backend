package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.dto.request.PostCommentCreate;
import com.fitnesscommerce.domain.post.dto.request.PostCommentUpdate;
import com.fitnesscommerce.domain.post.dto.request.PostSortFilter;
import com.fitnesscommerce.domain.post.dto.response.CustomPostCommentPageResponse;
import com.fitnesscommerce.domain.post.dto.response.IdResponse;
import com.fitnesscommerce.domain.post.dto.response.PostCommentResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.service.PostCommentService;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시글 댓글", description = "게시글 댓글 관련 API")
public class PostCommentApiController {

    private final PostCommentService postCommentService;

    @Operation(summary = "게시글 댓글 작성", description = "게시글에 댓글을 작성하는 API.")
    @ApiResponse(responseCode = "201", description = "게시글 댓글 작성 성공")
    @ApiResponse(responseCode = "404", description = "게시글 id를 찾을 수 없음")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<IdResponse> saveComment(
            @Parameter(name = "postId", description = "게시글 ID", in = ParameterIn.PATH) @PathVariable Long postId,
            @RequestBody PostCommentCreate postCommentCreate,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {
        IdResponse response = postCommentService.saveComment(postCommentCreate, session, postId);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/posts/{postId}/comments/{commentId}")
                .buildAndExpand(postId, response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "게시글 댓글 조회", description = "게시글의 댓글 목록을 조회하는 API.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CustomPostCommentPageResponse> getPostComment(
            @Parameter(name = "postId", description = "게시글 ID", in = ParameterIn.PATH) @PathVariable Long postId,
            @ModelAttribute PostSortFilter postSortFilter) {
        try {
            CustomPostCommentPageResponse response = postCommentService.getCommentPaging(postId, postSortFilter);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "게시글 댓글 수정", description = "게시글의 댓글을 수정하는 API.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "게시글 id를 찾을 수 없음")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<IdResponse> updatePostComment(
            @Parameter(name = "postId", description = "게시글 ID", in = ParameterIn.PATH) @PathVariable Long postId,
            @Parameter(name = "commentId", description = "댓글 ID", in = ParameterIn.PATH) @PathVariable Long commentId,
            @RequestBody PostCommentUpdate postCommentUpdate,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {
        IdResponse response = postCommentService.updateComment(postId, commentId, postCommentUpdate, session);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 댓글 삭제", description = "게시글의 댓글을 삭제하는 API.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "게시글 id를 찾을 수 없음")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deletePostComment(
            @Parameter(name = "postId", description = "게시글 ID", in = ParameterIn.PATH) @PathVariable Long postId,
            @Parameter(name = "commentId", description = "댓글 ID", in = ParameterIn.PATH) @PathVariable Long commentId,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {
        postCommentService.deleteComment(postId, commentId, session);
        return ResponseEntity.noContent().build();
    }
}
