package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.dto.request.PostCreate;
import com.fitnesscommerce.domain.post.dto.request.PostSortFilter;
import com.fitnesscommerce.domain.post.dto.request.PostUpdate;
import com.fitnesscommerce.domain.post.dto.response.CustomPostPageResponse;
import com.fitnesscommerce.domain.post.dto.response.IdResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.service.PostService;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시글", description = "게시글 관련 API")
public class PostApiController {

    private final PostService postService;

    @Operation(summary = "게시글 작성", description = "게시글 작성 API")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @ApiResponse(responseCode = "404", description = "게시판 카테고리 id를 찾을 수 없음")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/posts")
    public ResponseEntity<IdResponse> savePost(
            @ModelAttribute PostCreate postCreate,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {

        IdResponse response = postService.save(postCreate, session);

        return ResponseEntity.created(URI.create("/api/posts/" + response.getId())).body(response);
    }

    @Operation(summary = "게시글 단건 조회", description = "게시글 단건 조회 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "게시글 id를 찾을 수 없음")
    @GetMapping("/api/posts/{postId}")
    public PostResponse findOnePost(
            @Parameter(name = "postId", description = "게시글 id", in = ParameterIn.PATH) @PathVariable Long postId) {
        postService.updateViewCount(postId);
        return postService.findOnePost(postId);
    }

    @Operation(summary = "게시글 전체 조회", description = "게시글 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/api/posts")
    public ResponseEntity<CustomPostPageResponse> getAllPosts(
            @Parameter(name = "page&size&order", description = "요청할 page&size$order", in = ParameterIn.QUERY) @ModelAttribute PostSortFilter postSortFilter,
            @Parameter(name = "search", description = "검색요청할 search", in = ParameterIn.QUERY) @RequestParam(required = false) String search) {

        CustomPostPageResponse response = postService.getAllPostPaging(postSortFilter, search);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<IdResponse> updatePost(
            @Parameter(name = "postId", description = "게시글 id", in = ParameterIn.PATH) @PathVariable Long postId,
            @ModelAttribute PostUpdate postUpdate,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {

        IdResponse response = postService.updatePost(postId, postUpdate, session);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity deletePost(
            @Parameter(name = "postId", description = "게시글 id", in = ParameterIn.PATH) @PathVariable Long postId,
            @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) throws IOException {

        postService.deletePost(postId, session);
        return ResponseEntity.ok().build();
    }
}