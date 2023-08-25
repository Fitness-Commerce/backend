package com.fitnesscommerce.domain.member.controller;

import com.fitnesscommerce.domain.member.dto.request.MemberJoinRequest;
import com.fitnesscommerce.domain.member.dto.request.MemberLoginRequest;
import com.fitnesscommerce.domain.member.dto.response.JwtResponse;
import com.fitnesscommerce.domain.member.dto.response.RefreshResponse;
import com.fitnesscommerce.domain.member.service.MemberService;

import com.fitnesscommerce.global.config.data.MemberSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
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

    @PostMapping("/api/members/login")
    public ResponseEntity<JwtResponse> memberLogin(@RequestBody MemberLoginRequest request) {

        String[] token = memberService.login(request);
        String accessToken = token[0];
        String refreshToken = token[1];

        JwtResponse response = new JwtResponse(accessToken);

        //쿠키 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .secure(true)
                .build();

        System.out.println(accessToken);

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(response);
    }

    @GetMapping("/api/token/validate") //리프레시 토큰을 보내주면(새로운 액세스 토큰 발급 위함) -> 액세스 토큰이 만료되기 전에 요청
    public ResponseEntity<RefreshResponse> issueNewToken(@CookieValue("refreshToken") String refreshToken,
                                                         MemberSession session) {
        //todo 지금은 재발급을 요청을 할 때 refresh를 Authorization헤더에 담아 보내기 때문에 해당 사용자 인증에 필요한 accessToken이 없어서 인증이 안되어 요청이 거절됌.
        // 그래서 재발급 요청을 진행할 때, access는 그대로 Authorization헤더에 담아주고 cookie로 refresh를 담아서 서버에 보내주면
        // 서버에서 authorization헤더로 사용자를 검증하고, cookie의 refresh를 db에 저장된 refreshToken과 비교하여 일치하면
        // 새로운 accessToken을 발급해준다.
        String newAccessToken = memberService.validate(refreshToken, session);

        RefreshResponse response = new RefreshResponse(newAccessToken);

        System.out.println(newAccessToken);

        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/test") //권한 테스트를 위한 엔드포인트
    public void test1(MemberSession session) {
        System.out.println(12312313);
    }

}
