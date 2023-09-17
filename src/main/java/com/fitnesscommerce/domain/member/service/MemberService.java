package com.fitnesscommerce.domain.member.service;

import com.fitnesscommerce.domain.member.crypto.PasswordEncoder;
import com.fitnesscommerce.domain.member.domain.Address;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.dto.request.MemberEditPasswordRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberEditRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberJoinRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberSearch;
import com.fitnesscommerce.domain.member.dto.response.MemberResponse;
import com.fitnesscommerce.domain.member.exception.*;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.AppConfig;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;

    @Transactional
    public void signup(MemberJoinRequest request) {

        //중복 체크
        check_duplicates_join(request);

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



    private void check_duplicates_join(MemberJoinRequest request) {
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

    private void check_duplicates_edit(MemberEditRequest request, Member member) {
        String memberNickname = member.getNickname();
        String memberPhoneNumber = member.getPhoneNumber();

        Optional<Member> nickname = memberRepository.findByNickname(request.getNickname());
        Optional<Member> phoneNumber = memberRepository.findByPhoneNumber(request.getPhoneNumber());

        if (nickname.isPresent() && !request.getNickname().equals(memberNickname)) {
            throw new AlreadyExistsNickname();
        }

        if (phoneNumber.isPresent() && !request.getPhoneNumber().equals(memberPhoneNumber)) {
            throw new AlreadyExistsPhoneNumber();
        }
    }




    public MemberResponse findOne(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(IdNotFound::new);

        MemberResponse response = MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .role(member.getRole())
                .area_range(member.getArea_range())
                .created_at(member.getCreated_at())
                .updated_at(member.getUpdated_at())
                .build();


        return response;
    }

    public List<MemberResponse> findMembers(MemberSearch memberSearch) {

        return memberRepository.getList(memberSearch).stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }

    public MemberResponse findOneOwn(MemberSession session) {
        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);

        MemberResponse response = MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .role(member.getRole())
                .area_range(member.getArea_range())
                .created_at(member.getCreated_at())
                .updated_at(member.getUpdated_at())
                .build();

        return response;
    }
    @Transactional
    public void edit(MemberEditRequest request, MemberSession session) {
        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);

        check_duplicates_edit(request, member);

        member.editMemberInfo(request.getNickname(), request.getPhoneNumber(), request.getAddress());

        member.getArea_range().clear();

        for (String area : request.getArea_range()) {
            member.getArea_range().add(area);
        }
    }

    public void editValidate(MemberEditRequest request, MemberSession session) {
        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);

        check_duplicates_edit(request, member);

        System.out.println("check success");
    }

    @Transactional
    public void editPassword(MemberEditPasswordRequest request, MemberSession session) {
        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);

        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new VerifyPassword(); //비밀번호 변경할 때 입력한 현재 비밀번호가 잘못 입력됌.
        }
        if (passwordEncoder.matches(request.getChangePassword(), member.getPassword())) {
            throw new VerifyPassword(); //바꿀 비밀번호와 현재 비밀번호가 같으면 안됌.
        }
        if (!request.getChangePassword().equals(request.getConfirmedPassword())) {
            throw new VerifyPassword(); //바꿀 비밀번호와 비밀번호 확인이 같아야함.
        }

        String changeEncryptedPassword = passwordEncoder.encrypt(request.getChangePassword());

        member.changePassword(changeEncryptedPassword);

    }

    @Transactional
    public void delete(MemberSession session) {
        memberRepository.deleteById(session.id);

    }

}
