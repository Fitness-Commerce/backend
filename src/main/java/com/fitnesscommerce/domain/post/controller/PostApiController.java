package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.dto.request.PostCreate;
import com.fitnesscommerce.domain.post.dto.request.PostUpdate;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.service.PostService;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;

    //게시글 작성
    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Long> savePost(@ModelAttribute PostCreate postCreate, MemberSession session)throws IOException {
        return ResponseEntity.ok(postService.savePost(postCreate, session));
    }

    //게시글 단건 조회
    @GetMapping("/api/posts/{postId}")
    public PostResponse findOnePost(@PathVariable Long postId){
        postService.updateViewCount(postId);
        return postService.findOnePost(postId);
    }


    //게시글 전체 조회
    @GetMapping("/api/posts")
    public ResponseEntity<Page<PostResponse>> findAllPostPaging(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size
    ){
        Page<PostResponse> postResponsePage = postService.findAllPostPaging(page,size);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Previous-Page", postResponsePage.hasPrevious() ? postResponsePage.previousPageable().getPageNumber() + "" : "");
        headers.add("Next-Page", postResponsePage.hasNext() ? postResponsePage.nextPageable().getPageNumber() + "" : "");

        return ResponseEntity.ok().headers(headers).body(postResponsePage);
    }


    //게시글 수정
    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<Long> updatePost(@PathVariable Long postId, @ModelAttribute PostUpdate postUpdate,
                                           MemberSession session)throws IOException {
        return ResponseEntity.ok(postService.updatePost(postId,postUpdate));
    }


    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId) throws IOException {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

}
