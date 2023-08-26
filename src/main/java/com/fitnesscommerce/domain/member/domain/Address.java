package com.fitnesscommerce.domain.member.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @NotBlank(message = "주소를 입력해주세요.")
    private String front_address;

    @NotBlank(message = "주소를 입력해주세요.")
    private String detailed_address;

    @Builder
    public Address(String front_address, String detailed_address) {
        this.front_address = front_address;
        this.detailed_address = detailed_address;
    }
}
