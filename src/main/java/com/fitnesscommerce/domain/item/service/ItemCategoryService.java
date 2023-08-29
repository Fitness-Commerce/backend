package com.fitnesscommerce.domain.item.service;

import com.fitnesscommerce.domain.item.domain.ItemCategory;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryCreate;
import com.fitnesscommerce.domain.item.repository.ItemCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;

    @Transactional
    public void createCategory(ItemCategoryCreate request) {

        ItemCategory category = ItemCategory.builder()
                .title(request.getTitle())
                .build();

        itemCategoryRepository.save(category);
    }
}
