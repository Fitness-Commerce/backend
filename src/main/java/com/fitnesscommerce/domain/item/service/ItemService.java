package com.fitnesscommerce.domain.item.service;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemCategory;
import com.fitnesscommerce.domain.item.domain.ItemImage;
import com.fitnesscommerce.domain.item.domain.ItemStatus;
import com.fitnesscommerce.domain.item.dto.request.ItemCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemSortFilter;
import com.fitnesscommerce.domain.item.dto.request.ItemStatusUpdate;
import com.fitnesscommerce.domain.item.dto.request.ItemUpdate;
import com.fitnesscommerce.domain.item.dto.response.CustomItemPageResponse;
import com.fitnesscommerce.domain.item.dto.response.IdResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemResponse;
import com.fitnesscommerce.domain.item.exception.ItemCategoryNotFound;
import com.fitnesscommerce.domain.item.exception.ItemNotFound;
import com.fitnesscommerce.domain.item.repository.ItemCategoryRepository;
import com.fitnesscommerce.domain.item.repository.ItemImageRepository;
import com.fitnesscommerce.domain.item.repository.ItemRepository;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.AppConfig;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService  {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final MemberRepository memberRepository;
    private final AppConfig appConfig;

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    @Transactional
    public IdResponse save(ItemCreate itemCreate, MemberSession session) throws IOException {

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
                        .url("http://43.200.32.144:8080/api/item/images" + "/" + fileName)
                        .item(item)
                        .build();

                itemImageRepository.save(itemImage); //이미지 저장
            }
        }

        return IdResponse.builder()
                .id(saveItemd.getId())
                .build();
    }

    @Transactional
    public void updateViewCount(Long itemId) {
        itemRepository.updateViewCount(itemId);
    }


    public ItemResponse getItemResponseById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFound::new);
        List<ItemImage> itemImages = itemImageRepository.findByItemId(itemId);

        return ItemResponse.builder()
                .id(item.getId())
                .memberId(item.getMember().getId())
                .itemCategoryId(item.getItemCategory().getId())
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .itemPrice(item.getItemPrice())
                .itemStatus(item.getItemStatus())
                .buyerId(item.getBuyer() != null ? item.getBuyer().getId() : null)
                .itemImagesUrl(itemImages.stream().map(ItemImage::getUrl).collect(Collectors.toList()))
                .viewCount(item.getViewCount())
                .created_at(item.getCreated_at())
                .updated_at((item.getUpdated_at()))
                .nickName(memberRepository.getReferenceById(item.getMember().getId()).getNickname())
                .build();
    }


    public CustomItemPageResponse getAllItemPaging(ItemSortFilter itemSortFilter, String accessToken, String search) {

        String[] sortWord = itemSortFilter.getOrder().split("_");
        String orderBy = sortWord[0];
        String direction = sortWord[1];

        Sort.Order field = new Sort.Order(Sort.Direction.fromString(direction), orderBy);
        Sort sort = Sort.by(field);
        Pageable pageable = PageRequest.of(itemSortFilter.getPage()-1, itemSortFilter.getSize(), sort);
        Page<Item> itemsPage;

        if (accessToken != null) {

            String extractedToken = accessToken.substring("Bearer ".length());

            SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(extractedToken);

            Long memberId = claims.getBody().get("memberId", Long.class);
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다"));
            List<String> userAreas = member.getArea_range();

            if (search != null) {
                itemsPage = itemRepository.findByItemNameAndAreaRange(search, userAreas, pageable);
            }
             else {
                itemsPage = itemRepository.findAllByAreaRange(userAreas, pageable);
            }


        } else {
            if (search != null) {
                itemsPage = itemRepository.findByItemNameContainingAndItemStatusNot(search, ItemStatus.SOLD ,pageable);
            }
             else {
                itemsPage = itemRepository.findAllExcludeSold(pageable);
            }

        }

        List<ItemResponse> content = itemsPage.getContent().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        return new CustomItemPageResponse(itemsPage.getTotalPages(),content);
    }

    public ItemResponse mapItemToResponse(Item item) {
        List<ItemImage> itemImages = itemImageRepository.findByItemId(item.getId());

        List<String> firstImageUrl = new ArrayList<>();
        if (!itemImages.isEmpty()) {
            firstImageUrl.add(itemImages.get(0).getUrl());
        }
        return ItemResponse.builder()
                .id(item.getId())
                .memberId(item.getMember().getId())
                .itemCategoryId(item.getItemCategory().getId())
                .itemName(item.getItemName())
                .itemDetail(item.getItemDetail())
                .itemPrice(item.getItemPrice())
                .itemStatus(item.getItemStatus())
                .buyerId(item.getBuyer() != null ? item.getBuyer().getId() : null)
                .itemImagesUrl(firstImageUrl)
                .viewCount(item.getViewCount())
                .created_at(item.getCreated_at())
                .updated_at((item.getUpdated_at()))
                .nickName(memberRepository.getReferenceById(item.getMember().getId()).getNickname())
                .build();
    }


    @Transactional
    public IdResponse updateItem(Long itemId, ItemUpdate request, MemberSession session) throws IOException {
        // 수정할 Item을 조회합니다.
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);

        // 수정할 Item의 새로운 카테고리를 찾습니다.
        ItemCategory itemCategory = itemCategoryRepository.findByTitle(request.getCategoryTitle())
                .orElseThrow(ItemCategoryNotFound::new);

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new); //토큰이 1번 유저의 토큰 -> 1번 유저네? ㅇㅋ

        if(member == item.getMember()){

            // Item의 속성을 업데이트합니다.
            item.update(itemCategory, request.getItemName(), request.getItemDetail(),
                    request.getItemPrice());

            // 기존 이미지들을 삭제하고 Item의 이미지 컬렉션을 초기화합니다.
            List<ItemImage> byItemId = itemImageRepository.findByItemId(item.getId());
            if (byItemId != null) {
                for (ItemImage itemImage : byItemId) {
                    String fileName = itemImage.getFileName();
                    String filePath = fileStorageLocation + "/" + fileName;
                    Path targetLocation = Paths.get(filePath);
                    Files.deleteIfExists(targetLocation);
                    itemImageRepository.delete(itemImage);
                }
            }
            // 새로운 이미지를 저장합니다.
            if (request.getImages() != null) {

                for (MultipartFile image : request.getImages()) {
                    String originalFileName = image.getOriginalFilename();
                    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String fileName = UUID.randomUUID().toString() + extension;
                    String filePath = fileStorageLocation + "/" + fileName;

                    Path targetLocation = Paths.get(filePath);
                    Files.copy(image.getInputStream(), targetLocation);

                    ItemImage itemImage = ItemImage.builder()
                            .fileName(fileName)
                            .url("http://43.200.32.144:8080/api/item/images" + "/" + fileName)
                            .item(item)
                            .build();

                    itemImageRepository.save(itemImage);
                }
            }
            return IdResponse.builder()
                    .id(item.getId())
                    .build();
        }else
            throw new RuntimeException("회원이 일치하지 않습니다");

    }

    @Transactional
    public void delete(Long itemId, MemberSession session) throws IOException {
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFound::new);

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if(member == item.getMember()){
            List<ItemImage> byItemId = itemImageRepository.findByItemId(item.getId());

            for (ItemImage itemImage : byItemId) {

                String fileName = itemImage.getFileName();
                String filePath = fileStorageLocation + "/" + fileName;
                Path targetLocation = Paths.get(filePath);
                Files.deleteIfExists(targetLocation);

            }

            itemRepository.delete(item);
        }else
            throw new RuntimeException("회원이 일치하지 않습니다");
    }

    //itemStatus 업데이트
    @Transactional
    public void updateItemStatus(ItemStatusUpdate request, MemberSession session){

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        System.out.println(request.getItemId());
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(ItemNotFound::new);

        Member buyer = memberRepository.findById(request.getBuyerId())
                .orElseThrow(IdNotFound::new);

        if(member == item.getMember()){
            if(request.getItemStatus() == ItemStatus.SOLD){
                item.updateItemStatus(request.getItemId(), request.getItemStatus(), buyer);
            }else{
                item.updateItemStatus(request.getItemId(), request.getItemStatus(), null);
            }
        }else
            throw new RuntimeException("회원이 일치하지 않습니다");

    }
}
