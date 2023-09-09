package com.fitnesscommerce.domain.item.repository;

import com.fitnesscommerce.domain.item.domain.ItemComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCommentRepository extends JpaRepository<ItemComment, Long> {

    Page<ItemComment> findByItemId(Long itemId, Pageable pageable);
}
