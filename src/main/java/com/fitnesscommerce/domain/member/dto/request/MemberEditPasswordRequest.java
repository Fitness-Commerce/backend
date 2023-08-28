package com.fitnesscommerce.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberEditPasswordRequest {

    private String currentPassword;
    private String changePassword;
    private String confirmedPassword;

}
