package com.fitnesscommerce.domain.member.dto.request;

import com.fitnesscommerce.domain.member.domain.Address;
import com.fitnesscommerce.domain.member.domain.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
public class MemberJoinRequest {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, message = "비밀번호는 최소 8자리입니다.")
    private String password;

    @NotBlank(message = "번호를 입력해주세요.")
    @Length(min = 11, max = 11, message = "올바른 번호가 아닙니다.")
    private String phoneNumber;

    @NotBlank(message = "이름을 입력해주세요.")
    @Length(min = 2, message = "이름은 최소 2자리입니다.")
    private String username;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Length(min = 2, message = "닉네임은 최소 2자리입니다.")
    private String nickname;

    @NotNull(message = "유저 타입을 선택해주세요.")
    private Role role;

    @Valid
    private Address address;

    @NotNull(message = "거래 지역을 선택해주세요.")
    private String[] area_range;


}
