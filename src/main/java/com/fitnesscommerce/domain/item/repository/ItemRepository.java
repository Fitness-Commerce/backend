package com.fitnesscommerce.domain.item.repository;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemCategory;

import com.fitnesscommerce.domain.item.domain.ItemStatus;

import com.fitnesscommerce.domain.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Modifying
    @Query(value = "update Item i set i.viewCount=i.viewCount+1 where i.id = ?1")
    void updateViewCount(Long itemId);

    Page<Item> findByItemCategoryAndItemStatusNot(ItemCategory itemCategory, ItemStatus itemStatus, Pageable pageable);

    @Query("SELECT DISTINCT i FROM Item i JOIN i.member m JOIN m.area_range ar WHERE ar IN :areas AND i.itemName LIKE CONCAT('%', :search ,'%') AND i.itemStatus != 'SOLD'")
    Page<Item> findByItemNameAndAreaRange(String search, List<String> areas, Pageable pageable);

    @Query("SELECT DISTINCT i FROM Item i JOIN i.member m JOIN m.area_range ar WHERE ar IN :areas AND i.itemStatus != 'SOLD'")
    Page<Item> findAllByAreaRange(List<String> areas, Pageable pageable);

    Page<Item> findByItemNameContainingAndItemStatusNot(String search, ItemStatus itemStatus ,Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.itemStatus != 'SOLD'")
    Page<Item> findAllExcludeSold(Pageable pageable);

    List<Item> findByMember(Member member);


}
