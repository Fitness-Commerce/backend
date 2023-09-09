package com.fitnesscommerce.domain.item.service;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.item.domain.ItemComment;
import com.fitnesscommerce.domain.item.dto.request.ItemCommentCreate;
import com.fitnesscommerce.domain.item.dto.request.ItemCommentUpdate;
import com.fitnesscommerce.domain.item.dto.response.CustomItemCommentPageResponse;
import com.fitnesscommerce.domain.item.dto.response.ItemCommentResponse;
import com.fitnesscommerce.domain.item.exception.ItemCommentNotFound;
import com.fitnesscommerce.domain.item.exception.ItemNotFound;
import com.fitnesscommerce.domain.item.repository.ItemCommentRepository;
import com.fitnesscommerce.domain.item.repository.ItemRepository;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemCommentService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final ItemCommentRepository itemCommentRepository;

    @Transactional
    public Map<String, Long> createComment(ItemCommentCreate request, MemberSession session, Long itemId) throws IOException {

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFound::new);

        ItemComment comment = ItemComment.builder()
                .member(member)
                .item(item)
                .content(request.getContent())
                .build();

        Map<String, Long> response = new HashMap<>();

        response.put("id", itemCommentRepository.save(comment).getId());
        return response;
    }

    @Transactional
    public Map<String, Long> updateComment(ItemCommentUpdate request, Long itemId, Long commentId, MemberSession session){

        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFound::new);
        ItemComment itemComment = itemCommentRepository.findById(commentId)
                .orElseThrow(ItemCommentNotFound::new);
        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if(member == itemComment.getMember()){
            itemComment.updateComment(request.getContent());
            Map<String, Long> response = new HashMap<>();

            response.put("id", itemComment.getId());
            return response;
        }else
            throw new RuntimeException("회원이 일치하지 않습니다");



    }

    @Transactional
    public void deleteComment(Long commentId, Long itemId, MemberSession session){

        ItemComment itemComment = itemCommentRepository.findById(commentId)
                .orElseThrow(ItemCommentNotFound::new);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFound::new);
        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if(member == itemComment.getMember()){
            itemCommentRepository.delete(itemComment);
        }else
            throw new RuntimeException("회원이 일치하지 않습니다");
    }

    public CustomItemCommentPageResponse getCommentsByItem(Long itemId, int page, int size, String orderBy, String direction) {
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(direction), orderBy);
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ItemComment> commentsPage = itemCommentRepository.findByItemId(itemId, pageable);

        List<ItemCommentResponse> content = commentsPage.getContent().stream()
                .map(this::mapCommentToResponse)
                .collect(Collectors.toList());

        return new CustomItemCommentPageResponse(commentsPage.getTotalPages(), content);
    }

    public ItemCommentResponse mapCommentToResponse(ItemComment comment) {
        return ItemCommentResponse.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .itemId(comment.getItem().getId())
                .content(comment.getContent())
                .created_at(comment.getCreated_at())
                .updated_at(comment.getUpdated_at())
                .nickName(memberRepository.getReferenceById(comment.getMember().getId()).getNickname())
                .build();
    }

}
