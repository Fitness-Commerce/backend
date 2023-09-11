package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.dto.request.PostCategoryCreate;
import com.fitnesscommerce.domain.post.dto.request.PostCategoryUpdate;
import com.fitnesscommerce.domain.post.dto.request.PostSortFilter;
import com.fitnesscommerce.domain.post.dto.response.CustomPostPageResponse;
import com.fitnesscommerce.domain.post.dto.response.IdResponse;
import com.fitnesscommerce.domain.post.dto.response.PostCategoryResponse;
import com.fitnesscommerce.domain.post.service.PostCategoryService;
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

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시판", description = "게시판 관련 API")
public class PostCategoryApiController {

    private final PostCategoryService postCategoryService;

    @Operation(summary = "게시판 생성", description = "게시판 생성 API")
    @ApiResponse(responseCode = "201", description = "게시판 생성 성공")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @PostMapping("/api/postCategories")
    public ResponseEntity<IdResponse> saveCategoryPost(
            @RequestBody PostCategoryCreate postCategoryCreate) throws IOException {

        IdResponse response = postCategoryService.createCategory(postCategoryCreate);

        return ResponseEntity.created(URI.create("/api/postCategories" + response.getId())).body(response);
    }

    @Operation(summary = "게시판 목록 조회", description = "모든 게시판 목록을 조회하는 API.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/api/postCategories")
    public ResponseEntity<Object> getAllCategories() {
        List<PostCategoryResponse> postCategoryResponses = postCategoryService.getAllCategories();
        return ResponseEntity.ok(postCategoryResponses);
    }

    @Operation(summary = "게시판별 게시글 조회", description = "특정 게시판의 게시글을 조회하는 API.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "게시글 id를 찾을 수 없음")
    @GetMapping("/api/postCategories/{postCategoryId}/posts")
    public ResponseEntity<CustomPostPageResponse> getPostsByCategory(
            @Parameter(name = "카테고리 ID", description = "게시판 카테고리 ID", in = ParameterIn.PATH) @PathVariable Long postCategoryId,
            @ModelAttribute PostSortFilter postSortFilter) {
        try {
            CustomPostPageResponse response = postCategoryService.getPostsByCategoryPaging(postCategoryId, postSortFilter);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "게시판 수정", description = "게시판을 수정하는 API.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @PutMapping("/api/postCategories/{postCategoryId}")
    public ResponseEntity<IdResponse> updatePostCategory(
            @Parameter(name = "카테고리 ID", description = "게시판 카테고리 ID", in = ParameterIn.PATH) @PathVariable Long postCategoryId,
            @RequestBody PostCategoryUpdate postCategoryUpdate) {
        IdResponse response = postCategoryService.updateCategory(postCategoryUpdate, postCategoryId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시판 삭제", description = "게시판을 삭제하는 API.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    @DeleteMapping("/api/postCategories/{postCategoryId}")
    public ResponseEntity deletePostCategory(
            @Parameter(name = "카테고리 ID", description = "게시판 카테고리 ID", in = ParameterIn.PATH) @PathVariable Long postCategoryId) {
        postCategoryService.deleteCategory(postCategoryId);
        return ResponseEntity.ok().build();
    }
}
