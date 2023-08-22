package com.fitnesscommerce.domain.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class JwtResponse {

    private final String accessToken;

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
