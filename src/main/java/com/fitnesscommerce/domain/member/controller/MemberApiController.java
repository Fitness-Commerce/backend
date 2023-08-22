package com.fitnesscommerce.domain.member.controller;

import com.fitnesscommerce.domain.member.dto.request.MemberJoinRequest;
import com.fitnesscommerce.domain.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/members/signup")
    public ResponseEntity create(@RequestBody @Valid MemberJoinRequest request) {

        memberService.signup(request);

        return ResponseEntity.created(URI.create("/api/members/join")).build();
    }


}
