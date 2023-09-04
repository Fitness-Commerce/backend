package com.fitnesscommerce.domain.item.service;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemCategory;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryUpdate;
import com.fitnesscommerce.domain.item.dto.response.CustomItemPageResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemCategoryResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.exception.ItemCategoryNotFound;
import com.fitnesscommerce.domain.item.exception.ItemNotFound;
import com.fitnesscommerce.domain.item.repository.ItemCategoryRepository;
import com.fitnesscommerce.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @Transactional
    public Long createCategory(ItemCategoryCreate request) {

        ItemCategory category = ItemCategory.builder()
                .title(request.getTitle())
                .build();

        return itemCategoryRepository.save(category).getId();
    }

    @Transactional
    public Long updateCategory(ItemCategoryUpdate request,Long categoryId) {

        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템 카테고리를 찾을 수 없습니다."));

        itemCategory.updateCategory(request.getTitle());

        return itemCategory.getId();
    }

    @Transactional
    public void deleteCategory(Long categoryId){
        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId)
                .orElseThrow(ItemCategoryNotFound::new);
        itemCategoryRepository.delete(itemCategory);
    }

    public List<ItemCategoryResponse> getAllCategories() {
        List<ItemCategory> categories = itemCategoryRepository.findAll();

        return categories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ItemCategoryResponse mapToResponse(ItemCategory category) {
        return new ItemCategoryResponse(
                category.getId(),
                category.getTitle(),
                category.getItems().stream().map(Item::getId).collect(Collectors.toList()),
                category.getCreated_at(),
                category.getUpdated_at()
        );
    }

    public CustomItemPageResponse getItemsByCategoryPaging(Long categoryId, int page, int size, String orderBy, String direction) {
        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템 카테고리를 찾을 수 없습니다."));

        Sort.Order order = new Sort.Order(Sort.Direction.fromString(direction), orderBy);
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Item> itemsPage = itemRepository.findByItemCategory(itemCategory, pageable);

        List<ItemResponse> content = itemsPage.getContent().stream()
                .map(itemService::mapItemToResponse)
                .collect(Collectors.toList());

        return new CustomItemPageResponse(itemsPage.getTotalPages(), content);
    }




}
