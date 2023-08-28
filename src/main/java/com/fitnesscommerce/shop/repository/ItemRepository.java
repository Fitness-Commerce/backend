package com.fitnesscommerce.shop.repository;


import com.fitnesscommerce.shop.domain.Item;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Modifying
    @Query(value = "update Item i set i.viewCount=i.viewCount+1 where i.id = ?1")
    void updateViewCount(Long itemId);
}