package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.dto.request.PostCommentCreate;
import com.fitnesscommerce.domain.post.dto.request.PostCommentUpdate;
import com.fitnesscommerce.domain.post.dto.response.PostCommentResponse;
import com.fitnesscommerce.domain.post.service.PostCommentService;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostCommentApiController {

    private final PostCommentService postCommentService;


    // 게시글 댓글 작성
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<PostCommentResponse> saveComment(@PathVariable Long postId,
                                                           @RequestBody PostCommentCreate postCommentCreate,
                                                           MemberSession session){
        PostCommentResponse postCommentResponse = postCommentService.saveComment(postId,postCommentCreate,session);
        return ResponseEntity.ok(postCommentResponse);
    }

    /*// 게시글 댓글 조회
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Page<PostResponse>> getPostsByCommentPaging(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostResponse> posts = postCommentService.getPostCommentPaging(commentId, page, size);
        return ResponseEntity.ok(posts);
    }*/

    // 게시글 댓글 수정
    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Long> updatePostComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody PostCommentUpdate postCommentUpdate,
            MemberSession session
    ) {
        Long postCommentResponse = postCommentService.updateComment(postId, commentId, postCommentUpdate, session);
        return ResponseEntity.ok(postCommentResponse);
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
