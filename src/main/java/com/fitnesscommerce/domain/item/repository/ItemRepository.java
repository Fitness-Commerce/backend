package com.fitnesscommerce.domain.item.repository;

import com.fitnesscommerce.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Modifying
    @Query(value = "update Item i set i.viewCount=i.viewCount+1 where i.id = ?1")
    void updateViewCount(Long itemId);
}
