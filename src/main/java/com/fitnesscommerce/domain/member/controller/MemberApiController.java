package com.fitnesscommerce.domain.member.controller;

import com.fitnesscommerce.domain.member.dto.request.MemberEditPasswordRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberEditRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberJoinRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberSearch;
import com.fitnesscommerce.domain.member.dto.response.MemberResponse;
import com.fitnesscommerce.domain.member.service.MemberService;

import com.fitnesscommerce.global.config.data.MemberSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/members/signup")
    public ResponseEntity create(@RequestBody @Valid MemberJoinRequest request) {

        memberService.signup(request);

        return ResponseEntity.created(URI.create("/api/members/join")).build();
    }

    @GetMapping("/api/members/{memberId}")
    public MemberResponse getMember(@PathVariable Long memberId) {
        MemberResponse response = memberService.findOne(memberId);
        return response;
    }


    @GetMapping("/api/members") //todo querydsl
    public List<MemberResponse> getMembers(@ModelAttribute MemberSearch memberSearch) {
        return memberService.findMembers(memberSearch);
    }

    @GetMapping("/api/members/mypage")
    public MemberResponse getMemberOwn(MemberSession session) {
        MemberResponse response = memberService.findOneOwn(session);
        return response;
    }

    @PutMapping("/api/members/{memberId}")
    public ResponseEntity editMember(@RequestBody MemberEditRequest request,
                                     @PathVariable Long memberId,
                                     MemberSession session) {
        memberService.edit(request, memberId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/members/{memberId}/password")
    public ResponseEntity editMemberPassword(@RequestBody MemberEditPasswordRequest request,
                                             @PathVariable Long memberId,
                                             MemberSession session) {
        memberService.editPassword(request, memberId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/members/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId,
                                       MemberSession session) {
        memberService.delete(memberId);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/test") //권한 테스트를 위한 엔드포인트
    public void test1(MemberSession session) {
        System.out.println(12312313);
    }



}
