package com.fitnesscommerce.shop.repository;

import com.fitnesscommerce.shop.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

}