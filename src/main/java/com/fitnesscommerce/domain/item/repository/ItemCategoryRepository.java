package com.fitnesscommerce.domain.item.repository;

import com.fitnesscommerce.domain.item.domain.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    Optional<ItemCategory> findByTitle(String title);
}
