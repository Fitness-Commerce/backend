package com.fitnesscommerce.domain.post.controller;

import com.fitnesscommerce.domain.post.dto.request.PostCreate;
import com.fitnesscommerce.domain.post.dto.request.PostSortFilter;
import com.fitnesscommerce.domain.post.dto.request.PostUpdate;
import com.fitnesscommerce.domain.post.dto.response.CustomPostPageResponse;
import com.fitnesscommerce.domain.post.dto.response.IdResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.service.PostService;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;

    //게시글 작성
    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<IdResponse> savePost(@ModelAttribute PostCreate postCreate, MemberSession session)throws IOException {

        IdResponse response = postService.save(postCreate, session);

        return ResponseEntity.created(URI.create("/api/posts/" + response.getId())).body(response);
    }

    //게시글 단건 조회
    @GetMapping("/api/posts/{postId}")
    public PostResponse findOnePost(@PathVariable Long postId){
        postService.updateViewCount(postId);
        return postService.findOnePost(postId);
    }


    //게시글 전체 조회
    @GetMapping("/api/posts")
    public ResponseEntity<CustomPostPageResponse> getAllPosts(@ModelAttribute PostSortFilter postSortFilter,
                                                              @RequestParam(required = false) String search) {
        //System.out.println(search);
        CustomPostPageResponse response = postService.getAllPostPaging(postSortFilter, search);
        return ResponseEntity.ok(response);
    }


    //게시글 수정
    @PutMapping("/api/posts/{postId}")
    public ResponseEntity<IdResponse> updatePost(@PathVariable Long postId, @ModelAttribute PostUpdate postUpdate,
                                                 MemberSession session)throws IOException {
        IdResponse response = postService.updatePost(postId, postUpdate, session);


        return ResponseEntity.ok(response);
    }


    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId, MemberSession session) throws IOException {
        postService.deletePost(postId, session);
        return ResponseEntity.ok().build();
    }

}
