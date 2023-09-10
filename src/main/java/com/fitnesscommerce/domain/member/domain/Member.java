package com.fitnesscommerce.domain.member.domain;

import com.fitnesscommerce.domain.item.domain.Item;
import com.fitnesscommerce.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String phoneNumber;

    private String username;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;

    @ElementCollection
    @Column(name = "area_range")
    private List<String> area_range = new ArrayList<>();

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    @Builder
    public Member(String email, String password, String phoneNumber,
                  String username, String nickname, Role role, Address address) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.nickname = nickname;
        this.role = role;
        this.address = address;
        this.created_at = LocalDateTime.now();
    }

    public void editMemberInfo(String nickname, String phoneNumber, Address address) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
