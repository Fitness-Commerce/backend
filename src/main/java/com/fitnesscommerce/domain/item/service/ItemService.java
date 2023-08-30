package com.fitnesscommerce.domain.item.service;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemCategory;
import com.fitnesscommerce.domain.item.domain.ItemImage;
import com.fitnesscommerce.domain.item.dto.request.ItemCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemUpdate;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.exception.ItemCategoryNotFound;
import com.fitnesscommerce.domain.item.exception.ItemNotFound;
import com.fitnesscommerce.domain.item.repository.ItemCategoryRepository;
import com.fitnesscommerce.domain.item.repository.ItemImageRepository;
import com.fitnesscommerce.domain.item.repository.ItemRepository;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService  {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final MemberRepository memberRepository;

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @Transactional
    public Long save(ItemCreate itemCreate, MemberSession session) throws IOException {

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);
        ItemCategory itemCategory = itemCategoryRepository.findByTitle(itemCreate.getCategoryTitle())
                .orElseThrow(ItemCategoryNotFound::new);

        Item item = Item.builder()
                .member(member)
                .itemCategory(itemCategory)
                .itemName(itemCreate.getItemName())
                .itemDetail(itemCreate.getItemDetail())
                .itemPrice(itemCreate.getItemPrice())
                .build();

        Item saveItemd = itemRepository.save(item); //아이템 저장

        if(itemCreate.getImages() != null){
            for (MultipartFile image : itemCreate.getImages()) {

                String originalFileName = image.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + extension;
                String filePath = fileStorageLocation + "/" + fileName;

                Path targetLocation = Paths.get(filePath);
                Files.copy(image.getInputStream(), targetLocation);

                ItemImage itemImage = ItemImage.builder()
                        .fileName(fileName)
                        .url("http://localhost:8080/api/item/images" + "/" + fileName)
                        .item(item)
                        .build();

                itemImageRepository.save(itemImage); //이미지 저장

                item.addItemImage(itemImage);//아이템에 이미지 저장
            }
        }

        itemCategory.addItem(item);

        return saveItemd.getId();

    }

    @Transactional
    public void updateViewCount(Long itemId) {
        itemRepository.updateViewCount(itemId);
    }


    public ItemResponse getItemResponseById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFound::new);

        return ItemResponse.builder()
                .id(item.getId())
                .memberId(item.getMember().getId())
                .itemCategoryId(item.getItemCategory().getId())
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .itemPrice(item.getItemPrice())
                .itemStatus(item.getItemStatus())
                .itemImagesUrl(item.getItemImages().stream().map(ItemImage::getUrl).collect(Collectors.toList()))
                .viewCount(item.getViewCount())
                .created_at(item.getCreated_at())
                .updated_at((item.getUpdated_at()))
                .build();
    }



    public Page<ItemResponse> getAllItemPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemsPage = itemRepository.findAll(pageable);

        Page<ItemResponse> itemResponsesPage = itemsPage.map(this::mapItemToResponse);

        return PageableExecutionUtils.getPage(itemResponsesPage.getContent(), pageable, itemsPage::getTotalElements);
    }

    public ItemResponse mapItemToResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .memberId(item.getMember().getId())
                .itemCategoryId(item.getItemCategory().getId())
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .itemPrice(item.getItemPrice())
                .itemStatus(item.getItemStatus())
                .itemImagesUrl(item.getItemImages().stream().map(ItemImage::getUrl).collect(Collectors.toList()))
                .viewCount(item.getViewCount())
                .created_at(item.getCreated_at())
                .updated_at((item.getUpdated_at()))
                .build();
    }


    @Transactional
    public Long updateItem(Long itemId, ItemUpdate request) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);
        ItemCategory itemCategory = itemCategoryRepository.findByTitle(request.getCategoryTitle())
                .orElseThrow(ItemCategoryNotFound::new);

        if(item.getItemCategory().getTitle().equals(request.getCategoryTitle()))  //카테고리 변경되었다면 카테고리에서 item데이터삭제
            item.getItemCategory().removeItem(item);

        item.update(itemCategory, request.getItemName(), request.getItemDetail(),
                request.getItemPrice(), request.getItemStatus());

        //기존 itemImageRepository의 itemId가 같은 image데이터 삭제
        List<ItemImage> byItemId = itemImageRepository.findByItemId(item.getId());

        for (ItemImage itemImage : byItemId) {

            String fileName = itemImage.getFileName();
            String filePath = fileStorageLocation + "/" + fileName;
            Path targetLocation = Paths.get(filePath);
            Files.deleteIfExists(targetLocation);

        }

        item.getItemImages().clear();

        if(request.getImages() != null){
            for (MultipartFile image : request.getImages()) {

                String originalFileName = image.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + extension;
                String filePath = fileStorageLocation + "/" + fileName;

                Path targetLocation = Paths.get(filePath);
                Files.copy(image.getInputStream(), targetLocation);

                ItemImage itemImage = ItemImage.builder()
                        .fileName(fileName)
                        .url("http://localhost:8080/api/item/images" + "/" + fileName)
                        .item(item)
                        .build();

                itemImageRepository.save(itemImage); //이미지 저장

                item.addItemImage(itemImage);//아이템에 이미지 저장
            }
        }

        itemCategory.addItem(item);  //카테고리에 수정한 상품 저장

        return item.getId();

    }

    @Transactional
    public void delete(Long itemId) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);

        List<ItemImage> byItemId = itemImageRepository.findByItemId(item.getId());

        for (ItemImage itemImage : byItemId) {

            String fileName = itemImage.getFileName();
            String filePath = fileStorageLocation + "/" + fileName;
            Path targetLocation = Paths.get(filePath);
            Files.deleteIfExists(targetLocation);

        }

        ItemCategory category = item.getItemCategory();
        category.removeItem(item); // 카테고리에서 상품 제거

        itemRepository.delete(item);
    }
}
