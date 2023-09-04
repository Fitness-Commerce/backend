package com.fitnesscommerce.domain.item.repository;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemCategory;
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

    Page<Item> findByItemCategory(ItemCategory itemCategory, Pageable pageable);

    // search가 주어지고 area_range에 맞는 아이템을 검색하고 정렬하는 경우
    @Query("SELECT DISTINCT i FROM Item i JOIN i.member m JOIN m.area_range ar WHERE ar IN :areas AND i.itemName LIKE CONCAT('%', :search ,'%')")
    Page<Item> findByItemNameAndAreaRange(String search, List<String> areas, Pageable pageable);

    // search 없이 area_range에 맞는 아이템만 검색하고 정렬하는 경우
    @Query("SELECT DISTINCT i FROM Item i JOIN i.member m JOIN m.area_range ar WHERE ar IN :areas")
    Page<Item> findAllByAreaRange(List<String> areas, Pageable pageable);

    // 로그인되지 않은 사용자나 accessToken이 제공되지 않은 경우, search로 아이템 이름 검색 및 정렬
    Page<Item> findByItemNameContaining(String search, Pageable pageable);


}
