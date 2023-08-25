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

    @PostMapping(produces = "application/json;charset=UTF-8", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> savePost(
            @RequestParam Long postCategoryId,
            @RequestParam Long memberId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestPart List<MultipartFile> images
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
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create post");
        }
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PostRes getPost(@PathVariable Long postId){
        PostRes response = postService.findOnePost(postId);
        return response;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<PostRes> getAllPosts() {
        return postService.findAllPost();
    }

    @PutMapping(value = "/{postId}", produces = "application/json;charset=UTF-8", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestParam Long postCategoryId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestPart(required = false) List<MultipartFile> postImages
    ) {
        Post updatedPost = postService.updatePost(postId, postCategoryId, title, content, postImages);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok("Post and associated images deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete post: " + e.getMessage());
        }
    }



}
