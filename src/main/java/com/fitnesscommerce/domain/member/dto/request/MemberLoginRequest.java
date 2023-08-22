package com.fitnesscommerce.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class MemberLoginRequest {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Length(min = 8, message = "비밀번호는 최소 8자리입니다.")
    private String password;

    @Builder
    public MemberLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
