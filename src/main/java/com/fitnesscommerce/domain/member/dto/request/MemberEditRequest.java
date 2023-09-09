package com.fitnesscommerce.domain.member.dto.request;

import com.fitnesscommerce.domain.member.domain.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberEditRequest {

    @NotBlank(message = "번호를 입력해주세요.")
    @Length(min = 11, max = 11, message = "올바른 번호가 아닙니다.")
    private String phoneNumber;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Length(min = 2, message = "닉네임은 최소 2자리입니다.")
    private String nickname;

    @Valid
    private Address address;

    @NotNull(message = "거래 지역을 선택해주세요.")
    private String[] area_range;


}
