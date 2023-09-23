package com.fitnesscommerce.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter @Setter
public class FirstMessageCreate {

    private String roomName; //만들어서 주고
    private Long itemId; //아이템 상세에서 꽂아주고
    private String Nickname; //보내지말고
    private String message; //보내고
    private LocalDateTime messageTime; //보내지말고

}


