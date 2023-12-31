package com.fitnesscommerce.domain.item.repository;

import com.fitnesscommerce.domain.item.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long itemId); //그 아이템에 속한 이미지들
}
