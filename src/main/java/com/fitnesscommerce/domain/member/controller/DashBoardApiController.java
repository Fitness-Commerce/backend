package com.fitnesscommerce.domain.member.controller;

import com.fitnesscommerce.domain.member.dto.response.MemberPostsResponse;
import com.fitnesscommerce.domain.member.dto.response.MemberSalesResponse;
import com.fitnesscommerce.domain.member.service.DashBoardService;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "대시보드", description = "대시보드 관련 API")
@RestController
@RequiredArgsConstructor
public class DashBoardApiController {

    private final DashBoardService dashBoardService;

    @Operation(summary = "회원 판매 목록", description = "회원 판매 목록 API", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "404", description = "회원 정보 없음")
    })
    @GetMapping("/api/dashboard/mySales")
    public List<MemberSalesResponse> mySalesHistory(@Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {

        return dashBoardService.findSalesHistory(session);
    }

    @Operation(summary = "회원 게시글 목록", description = "회원 게시글 목록 API", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패"),
            @ApiResponse(responseCode = "404", description = "회원 정보 없음")
    })
    @GetMapping("/api/dashboard/myPost") //내가 올린 게시글 보기
    public List<MemberPostsResponse> myPostsHistory(@Parameter(name = "accessToken", description = "로그인 한 회원의 accessToken", in = ParameterIn.HEADER) MemberSession session) {

        return dashBoardService.findPostsHistory(session);
    }

}
