package com.fitnesscommerce.domain.post.service;

import com.fitnesscommerce.domain.post.domain.Post;
import com.fitnesscommerce.domain.post.domain.PostCategory;
import com.fitnesscommerce.domain.post.dto.request.PostCategoryCreate;
import com.fitnesscommerce.domain.post.dto.request.PostCategoryUpdate;
import com.fitnesscommerce.domain.post.dto.response.PostCategoryResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.exception.PostCategoryNotFound;
import com.fitnesscommerce.domain.post.repository.PostCategoryRepository;
import com.fitnesscommerce.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    // 게시글 카테고리 작성
    @Transactional
    public Long createCategory(PostCategoryCreate request){
        PostCategory category = PostCategory.builder()
                .title(request.getTitle())
                .build();

        return postCategoryRepository.save(category).getId();
    }

    // 게시글 카테고리 조회
    public List<PostCategoryResponse> getAllCategories(){
        List<PostCategory> categories = postCategoryRepository.findAll();

        return categories.stream()
                .map(this::mapToResponse) //mapToResponse 메서드를 사용하여 각 PostCategory 객체를 PostCategoryResponse 객체로 변환
                .collect(Collectors.toList());
    }

    private PostCategoryResponse mapToResponse(PostCategory category){
        return new PostCategoryResponse(
                category.getId(),
                category.getTitle(),
                category.getPosts().stream().map(Post::getId).collect(Collectors.toList()), //getPosts()를 호출하여 해당 카테고리에 연관된 게시글들의 목록을 가져옵니다.
                category.getCreated_at(),
                category.getUpdated_at()
        );
    }


    public Page<PostResponse> getPostsByCategoryPaging(Long categoryId, int page, int size){
        PostCategory postCategory = postCategoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글 카테고리를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByPostCategory(postCategory, pageable);

        Page<PostResponse> postResponsePage = postPage.map(postService::mapPostToResponse);
        return PageableExecutionUtils.getPage(postResponsePage.getContent(), pageable, postPage::getTotalElements);
    }

    // 게시글 카테고리 수정
    @Transactional
    public Long updateCategory(PostCategoryUpdate request, Long categoryId){
        PostCategory postCategory = postCategoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글 카테고리를 찾을 수 없습니다."));

        postCategory.updateCategory(request.getTitle());

        return postCategory.getId();
    }

    // 게시글 카테고리 삭제
    @Transactional
    public void deleteCategory(Long categoryId) {

        PostCategory postCategory = postCategoryRepository.findById(categoryId)
                .orElseThrow(PostCategoryNotFound::new);

        postCategoryRepository.delete(postCategory);

    }


}
