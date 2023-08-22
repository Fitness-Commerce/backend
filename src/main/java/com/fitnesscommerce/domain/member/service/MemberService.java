package com.fitnesscommerce.domain.member.service;

import com.fitnesscommerce.domain.member.crypto.PasswordEncoder;
import com.fitnesscommerce.domain.member.domain.Address;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.dto.request.MemberJoinRequest;
import com.fitnesscommerce.domain.member.exception.*;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void signup(MemberJoinRequest request) {

        //중복 체크
        check_duplicates(request);

        String encryptedPassword = passwordEncoder.encrypt(request.getPassword());

        //주소 생성
        Address address = Address.builder()
                .front_address(request.getAddress().getFront_address())
                .detailed_address(request.getAddress().getDetailed_address())
                .build();

        //회원 정보 생성
        Member member = Member.builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .phoneNumber(request.getPhoneNumber())
                .username(request.getUsername())
                .nickname(request.getNickname())
                .role(request.getRole())
                .address(address)
                .build();




        //거래 지역 생성
        for (String area : request.getArea_range()) {
            member.getArea_range().add(area);
        }

        memberRepository.save(member);
    }

    private void check_duplicates(MemberJoinRequest request) {
        Optional<Member> email = memberRepository.findByEmail(request.getEmail());
        if (email.isPresent()) {
            throw new AlreadyExistsEmail();
        }
        Optional<Member> nickname = memberRepository.findByNickname(request.getNickname());
        if (nickname.isPresent()) {
            throw new AlreadyExistsNickname();
        }
        Optional<Member> phoneNumber = memberRepository.findByPhoneNumber(request.getPhoneNumber());
        if (phoneNumber.isPresent()) {
            throw new AlreadyExistsPhoneNumber();
        }
    }
}
