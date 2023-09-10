package com.fitnesscommerce.domain.post.service;

import com.fitnesscommerce.domain.post.domain.Post;
import com.fitnesscommerce.domain.post.domain.PostCategory;
import com.fitnesscommerce.domain.post.dto.request.PostCategoryCreate;
import com.fitnesscommerce.domain.post.dto.request.PostCategoryUpdate;
import com.fitnesscommerce.domain.post.dto.request.PostSortFilter;
import com.fitnesscommerce.domain.post.dto.response.CustomPostPageResponse;
import com.fitnesscommerce.domain.post.dto.response.IdResponse;
import com.fitnesscommerce.domain.post.dto.response.PostCategoryResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.exception.PostCategoryNotFound;
import com.fitnesscommerce.domain.post.repository.PostCategoryRepository;
import com.fitnesscommerce.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public IdResponse createCategory(PostCategoryCreate request){
        PostCategory postCategory = PostCategory.builder()
                .title(request.getTitle())
                .build();

        PostCategory savePostCategory = postCategoryRepository.save(postCategory);

        return IdResponse.builder()
                .id(savePostCategory.getId())
                .build();
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
                category.getCreated_at(),
                category.getUpdated_at()
        );
    }


    public CustomPostPageResponse getPostsByCategoryPaging(Long categoryId, PostSortFilter postSortFilter){
        PostCategory postCategory = postCategoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글 카테고리를 찾을 수 없습니다."));

        String[] sortPost = postSortFilter.getOrder().split("_");
        String orderBy = sortPost[0];
        String direction = sortPost[1];

        Sort.Order field = new Sort.Order(Sort.Direction.fromString(direction), orderBy);
        Sort sort = Sort.by(field);
        Pageable pageable = PageRequest.of(postSortFilter.getPage()-1, postSortFilter.getSize(), sort);
        Page<Post> postPage = postRepository.findByPostCategory(postCategory, pageable);

        List<PostResponse> content = postPage.getContent().stream()
                .map(postService::mapPostToResponse)
                .collect(Collectors.toList());

        return new CustomPostPageResponse(postPage.getTotalPages(), content);
    }

    // 게시글 카테고리 수정
    @Transactional
    public IdResponse updateCategory(PostCategoryUpdate request, Long categoryId){
        PostCategory postCategory = postCategoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalArgumentException("해당 게시글 카테고리를 찾을 수 없습니다."));

        postCategory.updateCategory(request.getTitle());

        return IdResponse.builder()
                .id(postCategory.getId())
                .build();

    }

    // 게시글 카테고리 삭제
    @Transactional
    public void deleteCategory(Long categoryId) {

        PostCategory postCategory = postCategoryRepository.findById(categoryId)
                .orElseThrow(PostCategoryNotFound::new);

        postCategoryRepository.delete(postCategory);

    }

}
