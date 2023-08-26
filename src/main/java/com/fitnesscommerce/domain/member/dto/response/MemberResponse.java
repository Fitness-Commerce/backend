package com.fitnesscommerce.domain.member.dto.response;

import com.fitnesscommerce.domain.member.domain.Address;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MemberResponse {

    private Long id;
    private String email;
    private String username;
    private String nickname;
    private String phoneNumber;
    private Address address;
    private Role role;
    private List<String> area_range;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @Builder
    public MemberResponse(Long id, String email, String username, String nickname,
                          String phoneNumber, Address address, Role role, List<String> area_range,
                          LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
        this.area_range = area_range;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.phoneNumber = member.getPhoneNumber();
        this.address = member.getAddress();
        this.role = member.getRole();
        this.area_range = member.getArea_range();
        this.created_at = member.getCreated_at();
        this.updated_at = member.getUpdated_at();
    }
}
