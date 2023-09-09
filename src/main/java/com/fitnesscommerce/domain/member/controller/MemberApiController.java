package com.fitnesscommerce.domain.member.controller;

import com.fitnesscommerce.domain.member.dto.request.MemberEditPasswordRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberEditRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberJoinRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberSearch;
import com.fitnesscommerce.domain.member.dto.response.MemberResponse;
import com.fitnesscommerce.domain.member.service.MemberService;

import com.fitnesscommerce.global.config.data.MemberSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@Tag(name = "회원", description = "회원 관련 API")
@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입 API", responses = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400",
                    description = "회원가입 실패(이메일@, 비밀번호8자이상, 번호11자 및 이메일, 전화번호, 닉네임 중복") })
    @PostMapping("/api/members/signup")
    public ResponseEntity create(@RequestBody @Valid MemberJoinRequest request) {

        memberService.signup(request);

        return ResponseEntity.created(URI.create("/api/members/join")).build();
    }

    @Operation(summary = "회원 단건 조회", description = "회원 단건 조회 API", responses = {
            @ApiResponse(responseCode = "404", description = "회원 id를 찾을 수 없음")
    })
    @GetMapping("/api/members/{memberId}")
    public MemberResponse getMember(@Parameter(name = "memberId", description = "회원 id", in = ParameterIn.PATH) @PathVariable Long memberId) {
        MemberResponse response = memberService.findOne(memberId);
        return response;
    }

    @Operation(summary = "회원 전체 조회", description = "회원 전체 조회 API")
    @GetMapping("/api/members") //todo total page
    public List<MemberResponse> getMembers(@Parameter(name = "page&size", description = "요청할 page&size", in = ParameterIn.QUERY) @ModelAttribute MemberSearch memberSearch) {
        return memberService.findMembers(memberSearch);
    }

    @Operation(summary = "회원 본인 조회", description = "회원 본인 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    })
    @GetMapping("/api/members/myProfile")
    public MemberResponse getMemberOwn(@Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {
        MemberResponse response = memberService.findOneOwn(session);
        return response;
    }

    @Operation(summary = "회원 본인 정보 수정", description = "회원 본인 정보 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    })
    @PutMapping("/api/members/myProfile")
    public ResponseEntity editMember(@RequestBody MemberEditRequest request,
                                     @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {
        memberService.edit(request, session);

        return ResponseEntity.ok().build();
    }
    @Operation(summary = "회원 본인 비밀번호 수정", description = "회원 본인 비밀번호 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    })
    @PutMapping("/api/members/myProfile/password")
    public ResponseEntity editMemberPassword(@RequestBody MemberEditPasswordRequest request,
                                             @Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {
        memberService.editPassword(request, session);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패")
    })
    @DeleteMapping("/api/members/myProfile")
    public ResponseEntity deleteMember(@Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {

        memberService.delete(session);

        return ResponseEntity.ok().build();
    }


}
