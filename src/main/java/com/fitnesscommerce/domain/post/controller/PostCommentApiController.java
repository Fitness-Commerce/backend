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
public class PostCommentApiController {

    private final PostCommentService postCommentService;


    // 게시글 댓글 작성
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<IdResponse> saveComment(@PathVariable Long postId,
                                                  @RequestBody PostCommentCreate postCommentCreate,
                                                  MemberSession session) throws IOException {
        IdResponse response = postCommentService.saveComment(postCommentCreate, session, postId);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/posts/{postId}/comments/{commentId}")
                .buildAndExpand(postId, response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    // 게시글 댓글 조회
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CustomPostCommentPageResponse> getPostComment(
            @PathVariable Long postId,
            @ModelAttribute PostSortFilter postSortFilter) {
        try { // 게시글이 존재하지 않거나 댓글이 없을 경우에 대한 예외 처리
            CustomPostCommentPageResponse response = postCommentService.getCommentPaging(postId, postSortFilter);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // 게시글 댓글 수정
    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<IdResponse> updatePostComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody PostCommentUpdate postCommentUpdate,
            MemberSession session
    )throws IOException {

        IdResponse response = postCommentService.updateComment(postId,commentId,postCommentUpdate,session);

        return ResponseEntity.ok(response);
    }

    // 게시글 댓글 삭제
    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deletePostComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            MemberSession session
    ) {
        postCommentService.deleteComment(postId, commentId, session);
        return ResponseEntity.noContent().build();
    }

}
