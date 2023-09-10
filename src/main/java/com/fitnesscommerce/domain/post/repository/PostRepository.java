package com.fitnesscommerce.domain.post.repository;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.post.domain.Post;
import com.fitnesscommerce.domain.post.domain.PostCategory;
import com.fitnesscommerce.domain.post.domain.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query(value = "update Post p set p.viewCount = p.viewCount+1 where p.id = ?1")
    void updateViewCount(Long postId);

    Page<Post> findByPostCategory(PostCategory postCategory, Pageable pageable);

    // 로그인되지 않은 사용자나 accessToken이 제공되지 않은 경우, search로 게시글 제목 검색 및 정렬
    Page<Post> findByTitleContaining(String search, Pageable pageable);

}