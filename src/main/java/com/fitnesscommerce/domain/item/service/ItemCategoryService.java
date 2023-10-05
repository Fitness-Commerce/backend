package com.fitnesscommerce.domain.item.service;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemCategory;
import com.fitnesscommerce.domain.item.domain.ItemStatus;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCategoryUpdate;
import com.fitnesscommerce.domain.item.dto.request.ItemSortFilter;
import com.fitnesscommerce.domain.item.dto.response.CustomItemPageResponse;
import com.fitnesscommerce.domain.item.dto.response.IdResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemCategoryResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.exception.ItemCategoryNotFound;
import com.fitnesscommerce.domain.item.repository.ItemCategoryRepository;
import com.fitnesscommerce.domain.item.repository.ItemRepository;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.domain.Role;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final MemberRepository memberRepository;

    @Transactional
    public IdResponse createCategory(ItemCategoryCreate request, MemberSession session) {

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if(member.getRole() == Role.ADMIN){

            ItemCategory category = ItemCategory.builder()
                    .title(request.getTitle())
                    .build();


            Long categoryId=itemCategoryRepository.save(category).getId();

            return IdResponse.builder()
                    .id(categoryId)
                    .build();
        }else
            throw new RuntimeException("관리자가 아닙니다.");

    }

    @Transactional
    public IdResponse updateCategory(ItemCategoryUpdate request,Long categoryId,MemberSession session) {

        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId)
                .orElseThrow(ItemCategoryNotFound::new);

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if(member.getRole() == Role.ADMIN){

            itemCategory.updateCategory(request.getTitle());

            return IdResponse.builder()
                    .id(itemCategory.getId())
                    .build();
        }else
            throw new RuntimeException("관리자가 아닙니다.");

    }

    @Transactional
    public void deleteCategory(Long categoryId, MemberSession session){
        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId)
                .orElseThrow(ItemCategoryNotFound::new);
        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if(member.getRole() == Role.ADMIN){

            itemCategoryRepository.delete(itemCategory);
        }else
            throw new RuntimeException("관리자가 아닙니다.");
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
                category.getCreated_at(),
                category.getUpdated_at()
        );
    }

    public CustomItemPageResponse getItemsByCategoryPaging(Long categoryId, ItemSortFilter itemSortFilter) {
        ItemCategory itemCategory = itemCategoryRepository.findById(categoryId)
                .orElseThrow(ItemCategoryNotFound::new);

        String[] sortWord = itemSortFilter.getOrder().split("_");
        String orderBy = sortWord[0];
        String direction = sortWord[1];

        Sort.Order order = new Sort.Order(Sort.Direction.fromString(direction), orderBy);
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(itemSortFilter.getPage() - 1, itemSortFilter.getSize(), sort);
        Page<Item> itemsPage = itemRepository.findByItemCategoryAndItemStatusNot(itemCategory, ItemStatus.SOLD ,pageable);

        List<ItemResponse> content = itemsPage.getContent().stream()
                .map(itemService::mapItemToResponse)
                .collect(Collectors.toList());

        return new CustomItemPageResponse(itemsPage.getTotalPages(), content);
    }




}
