package com.fitnesscommerce.domain.item.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ItemCommentResponse {

    private Long id;
    private Long itemId;
    private Long memberId;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    //작성자 nickName
    private String nickName;

    @Builder
    public ItemCommentResponse(Long id, Long itemId, Long memberId, String content,
                               LocalDateTime created_at, LocalDateTime updated_at, String nickName){

        this.id = id;
        this.itemId = itemId;
        this.memberId = memberId;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.nickName = nickName;
    }
}
