package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.dto.request.PostCategoryCreate;
import com.fitnesscommerce.domain.post.dto.request.PostCategoryUpdate;
import com.fitnesscommerce.domain.post.dto.request.PostSortFilter;
import com.fitnesscommerce.domain.post.dto.response.CustomPostPageResponse;
import com.fitnesscommerce.domain.post.dto.response.IdResponse;
import com.fitnesscommerce.domain.post.dto.response.PostCategoryResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.service.PostCategoryService;
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
public class PostCategoryApiController {

    private final PostCategoryService postCategoryService;

    //게시판 생성
    @PostMapping("/api/postCategories")
    public ResponseEntity<IdResponse> saveCategoryPost(@RequestBody PostCategoryCreate postCategoryCreate) throws IOException {

        IdResponse response = postCategoryService.createCategory(postCategoryCreate);

        return ResponseEntity.created(URI.create("/api/postCategories" + response.getId())).body(response);
    }

    //게시판 목록 조회
    @GetMapping("/api/postCategories")
    public ResponseEntity<Object> getAllCategories(){
        List<PostCategoryResponse> postCategoryResponses = postCategoryService.getAllCategories();
        return ResponseEntity.ok(postCategoryResponses);
    }

    //게시판별 게시글 조회
    @GetMapping("/api/postCategories/{postCategoryId}/posts")
    public ResponseEntity<CustomPostPageResponse> getPostsByCategory(@ModelAttribute PostSortFilter postSortFilter,
                                                                     @PathVariable Long categoryId){
        try {
            CustomPostPageResponse response = postCategoryService.getPostsByCategoryPaging(categoryId, postSortFilter);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //게시판 수정
    @PutMapping("/api/postCategories/{postCategoryId}")
    public ResponseEntity<IdResponse> updatePostCategory(@RequestBody PostCategoryUpdate postCategoryUpdate,
                                         @PathVariable Long postCategoryId){
        IdResponse response = postCategoryService.updateCategory(postCategoryUpdate, postCategoryId);

        return ResponseEntity.ok(response);
    }

    //게시판 삭제
    @DeleteMapping("/api/postCategories/{postCategoryId}")
    public ResponseEntity deletePostCategory(@PathVariable Long postCategoryId){
        postCategoryService.deleteCategory(postCategoryId);
        return ResponseEntity.ok().build();
    }
}
