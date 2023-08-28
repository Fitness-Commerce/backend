package com.fitnesscommerce.domain.member.dto.request;

import com.fitnesscommerce.domain.member.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberEditRequest {

    private String nickName;
    private String phoneNumber;
    private Address address;
    private String[] area_range;


}
