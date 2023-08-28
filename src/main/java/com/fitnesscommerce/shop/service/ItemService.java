package com.fitnesscommerce.shop.service;

import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.shop.domain.Item;
import com.fitnesscommerce.shop.domain.ItemCategory;
import com.fitnesscommerce.shop.domain.ItemImage;
import com.fitnesscommerce.shop.dto.request.ItemCreate;
import com.fitnesscommerce.shop.dto.request.ItemUpdate;
import com.fitnesscommerce.shop.dto.response.ItemResponse;
import com.fitnesscommerce.shop.repository.ItemCategoryRepository;
import com.fitnesscommerce.shop.repository.ItemImageRepository;
import com.fitnesscommerce.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService  {

    private final ItemRepository itemRepository;

    private final ItemImageRepository itemImageRepository;

    private final ItemCategoryRepository itemCategoryRepository;

    private final MemberRepository memberRepository;

    @Value("${file.storage.location}")
    private String fileStorageLocation;


    @Transactional
    public Item saveItem(ItemCreate itemCreate) {

        Member member = memberRepository.findById(itemCreate.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다!"));
        ItemCategory itemCategory = itemCategoryRepository.findById(itemCreate.getItemCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다!"));

        Item item = Item.builder()
                .member(member)
                .itemCategory(itemCategory)
                .itemName(itemCreate.getItemName())
                .itemDetail(itemCreate.getItemDetail())
                .itemPrice(itemCreate.getItemPrice())
                .itemStatus(itemCreate.getItemStatus())
                .build();

        Item savedItem = itemRepository.save(item);

        // 이미지가 null값이 아닐때 처리
        if (itemCreate.getImages() != null) {
            List<MultipartFile> images = itemCreate.getImages();

            List<ItemImage> savedImages = saveImages(images, savedItem);

            savedItem.getItemImages().addAll(savedImages);
        }

        return savedItem;
    }

    @Transactional
    public List<ItemImage> saveImages(List<MultipartFile> images, Item item) {
        List<ItemImage> itemImages = new ArrayList<>();

        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            String filePath = fileStorageLocation + "/" + fileName;

            try {
                Path targetLocation = Paths.get(filePath);
                Files.copy(image.getInputStream(), targetLocation);

                ItemImage itemImage = ItemImage.builder()
                        .fileName(fileName)
                        .url("http://localhost:8080/api/item/images" + "/" + fileName)
                        .item(item) // Item 엔티티 설정
                        .build();

                itemImages.add(itemImage);
            } catch (IOException e) {
                e.printStackTrace();
                // 이미지 저장 실패에 대한 로직 처리
            }
        }
        return itemImageRepository.saveAll(itemImages);
    }

    //조회수 업데이트
    @Transactional
    public void updateViewCount(Long itemId) {
        itemRepository.updateViewCount(itemId);
    }

    @Transactional
    public ItemResponse getItemResponseById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다!"));

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
                .createdAt(item.getCreatedAt())
                .updatedAt((item.getUpdatedAt()))
                .build();
    }

    @Transactional
    public List<ItemResponse> getAllItemResponses() {
        List<Item> items = itemRepository.findAll();

        return items.stream()
                .map(item -> ItemResponse.builder()
                        .id(item.getId())
                        .memberId(item.getMember().getId())
                        .itemCategoryId(item.getItemCategory().getId())
                        .itemName(item.getItemName())
                        .itemDetail(item.getItemDetail())
                        .itemPrice(item.getItemPrice())
                        .itemStatus(item.getItemStatus())
                        .itemImagesUrl(item.getItemImages().stream().map(ItemImage::getUrl).collect(Collectors.toList()))
                        .viewCount(item.getViewCount())
                        .createdAt(item.getCreatedAt())
                        .updatedAt(item.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }


    @Transactional
    public Item updateItem(Long itemId, ItemUpdate itemUpdate) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다!"));
        ItemCategory itemCategory = itemCategoryRepository.findById(itemUpdate.getItemCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다!"));

        List<ItemImage> newImages = new ArrayList<>();

        // 기존 ItemImage들 삭제
        List<ItemImage> oldItemImages = item.getItemImages();
        if (!oldItemImages.isEmpty()) {
            deleteItemImageFiles(oldItemImages); // 이미지 파일 삭제

            itemImageRepository.deleteAll(oldItemImages); // 이미지 레코드 삭제
            item.getItemImages().clear(); // item 객체에서 이미지 리스트 비우기
        }

        // 새로운 ItemImage들 생성 및 연결
        List<MultipartFile> images = itemUpdate.getImages();
        if (images != null) {
            for (MultipartFile image : images) {
                String fileName = image.getOriginalFilename();
                String filePath = fileStorageLocation + "/" + fileName;

                try {
                    Path targetLocation = Paths.get(filePath);
                    Files.copy(image.getInputStream(), targetLocation);

                    ItemImage newItemImage = ItemImage.builder()
                            .fileName(fileName)
                            .url("http://localhost:8080/api/item/images" + "/" + fileName)
                            .item(item) // 연결할 Item 설정
                            .build();
                    newImages.add(newItemImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 이미지 저장 실패에 대한 로직 처리
                }
            }
        }

        // Item의 속성들 업데이트
        item.change(
                itemCategory,
                itemUpdate.getItemName(),
                itemUpdate.getItemDetail(),
                itemUpdate.getItemPrice(),
                itemUpdate.getItemStatus(),
                newImages
        );

        return itemRepository.save(item);
    }

    private void deleteItemImageFiles(List<ItemImage> itemImages) {
        for (ItemImage image : itemImages) {
            String fileName = image.getFileName();
            String filePath = fileStorageLocation + "/" + fileName;

            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
                // 파일 삭제 실패에 대한 로직 처리
            }
        }

    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다!"));

        List<ItemImage> itemImages = item.getItemImages();
        if (!itemImages.isEmpty()) {
            deleteItemImageFiles(itemImages); // 이미지 파일 삭제
            itemImageRepository.deleteAll(itemImages); // 이미지 레코드 삭제
        }

        itemRepository.delete(item);
    }

}