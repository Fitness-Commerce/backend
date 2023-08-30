package com.fitnesscommerce.domain.post.repository;

import com.fitnesscommerce.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Modifying
    @Query(value = "update Post p set p.viewCount = p.viewCount+1 where p.id = ?1")
    void updateViewCount(Long postId);
}
