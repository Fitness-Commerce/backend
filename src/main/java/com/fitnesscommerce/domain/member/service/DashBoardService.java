package com.fitnesscommerce.domain.member.service;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemImage;
import com.fitnesscommerce.domain.item.repository.ItemImageRepository;
import com.fitnesscommerce.domain.item.repository.ItemRepository;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.dto.response.MemberPostsResponse;
import com.fitnesscommerce.domain.member.dto.response.MemberSalesResponse;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.domain.post.repository.PostRepository;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DashBoardService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;


    public List<MemberSalesResponse> findSalesHistory(MemberSession session) {
        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);

        return itemRepository.findByMember(member).stream()
                .map(item -> {
                    List<ItemImage> images = itemImageRepository.findByItemId(item.getId());
                    String mainImageUrl = !images.isEmpty() ? images.get(0).getUrl() : null;
                    return new MemberSalesResponse(item, mainImageUrl);
                })
                .collect(Collectors.toList());


    }

    public List<MemberPostsResponse> findPostsHistory(MemberSession session) {
        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);

        return postRepository.findByMember(member).stream().
                map(MemberPostsResponse::new)
                .collect(Collectors.toList());

    }


}


