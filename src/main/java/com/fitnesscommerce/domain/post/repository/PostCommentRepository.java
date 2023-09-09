package com.fitnesscommerce.domain.post.repository;

import com.fitnesscommerce.domain.post.domain.PostComment;
import com.fitnesscommerce.domain.post.domain.PostImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Optional<PostComment> findByContent(String content);


    Page<PostComment> findByPostId(Long postId, Pageable pageable);
}
