package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.dto.request.PostCategoryCreate;
import com.fitnesscommerce.domain.post.dto.request.PostCategoryUpdate;
import com.fitnesscommerce.domain.post.dto.response.PostCategoryResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostCategoryApiController {
    private final PostCategoryService postCategoryService;

    //게시판 생성
    @PostMapping("/api/postCategories")
    public ResponseEntity<Long> saveCategoryPost(@RequestBody PostCategoryCreate postCategoryCreate){
        return ResponseEntity.ok(postCategoryService.createCategory(postCategoryCreate));
    }

    //게시판 목록 조회
    @GetMapping("/api/postCategories")
    public ResponseEntity<Object> getAllCategories(){
        List<PostCategoryResponse> postCategoryResponses = postCategoryService.getAllCategories();
        return ResponseEntity.ok(postCategoryResponses);
    }

    //게시판별 게시글 조회
    @GetMapping("/api/postCategories/{postCategoryId}/posts")
    public Page<PostResponse> getPostsByCategoryPaging(
            @PathVariable Long postCategoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size){
        return postCategoryService.getPostsByCategoryPaging(postCategoryId,page,size);
    }

    //게시판 수정
    @PutMapping("/api/postCategories/{postCategoryId}")
    public ResponseEntity<Long> updatePostCategory(@PathVariable Long postCategoryId,
                                                   @RequestBody PostCategoryUpdate postCategoryUpdate){
        return ResponseEntity.ok(postCategoryService.updateCategory(postCategoryUpdate,postCategoryId));
    }

    //게시판 삭제
    @DeleteMapping("/api/postCategories/{postCategoryId}")
    public ResponseEntity deletePostCategory(@PathVariable Long postCategoryId){
        postCategoryService.deleteCategory(postCategoryId);
        return ResponseEntity.ok().build();
    }
}
