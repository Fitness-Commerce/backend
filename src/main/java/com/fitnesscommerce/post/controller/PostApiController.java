package com.fitnesscommerce.post.controller;
import com.fitnesscommerce.post.domain.Post;
import com.fitnesscommerce.post.dto.request.PostCreate;
import com.fitnesscommerce.post.dto.request.PostUpdate;
import com.fitnesscommerce.post.dto.response.PostRes;
import com.fitnesscommerce.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;

    //게시글 작성
    @PostMapping(produces = "application/json;charset=UTF-8", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> savePost(
            @RequestParam Long postCategoryId,
            @RequestParam Long memberId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestPart(required = false) List<MultipartFile> images
    ) {
        PostCreate postCreate = PostCreate.builder()
                .postCategoryId(postCategoryId)
                .memberId(memberId)
                .title(title)
                .content(content)
                .images(images)
                .build();

        Post createdPost = postService.savePost(postCreate);

        if (createdPost != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("게시글 등록 완료!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시글 등록 실패!");
        }
    }

    //게시글 단건 조회
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PostRes> findOnePost(@PathVariable Long postId){
        postService.updateViewCount(postId);
        PostRes postRes = postService.findOnePost(postId);
        return ResponseEntity.ok(postRes);
    }


    //게시글 전체 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PostRes>> findAllPost(){
        List<PostRes> postRes = postService.findAllPost();
        return ResponseEntity.ok(postRes);
    }


    //게시글 수정
    @PutMapping(value = "/{postId}", produces = "application/json;charset=UTF-8", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostRes> updatePost(
            @PathVariable Long postId,
            @RequestParam("postCategoryId") Long postCategoryId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        PostUpdate postUpdate = PostUpdate.builder()
                .postCategoryId(postCategoryId)
                .title(title)
                .content(content)
                .images(images)
                .build();

        Post updatedPost = postService.updatePost(postId, postUpdate);
        PostRes res = postService.findOnePost(updatedPost.getId());

        return ResponseEntity.ok(res);
    }


    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok("게시글 삭제 완료");
    }

}
