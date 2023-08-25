package com.fitnesscommerce.domain.auth.controller;

import com.fitnesscommerce.domain.auth.service.AuthService;
import com.fitnesscommerce.domain.auth.dto.request.LoginRequest;
import com.fitnesscommerce.domain.auth.dto.response.JwtResponse;
import com.fitnesscommerce.domain.auth.dto.response.RefreshResponse;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<JwtResponse> memberLogin(@RequestBody LoginRequest request) {

        String[] token = authService.login(request);
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

        return ResponseEntity.ok()
                .header("Set-Cookie", refreshTokenCookie.toString())
                .body(response);
    }

    @GetMapping("/api/auth/token-refresh") //리프레시 토큰을 보내주면(새로운 액세스 토큰 발급 위함) -> 액세스 토큰이 만료되기 전에 요청
    public ResponseEntity<RefreshResponse> issueNewToken(@CookieValue("refreshToken") String refreshToken) {

        String newAccessToken = authService.validate(refreshToken);

        RefreshResponse response = new RefreshResponse(newAccessToken);

        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String accessToken,
                                 @CookieValue("refreshToken") String refreshToken,
                                 MemberSession session) {

        authService.expireToken(accessToken, refreshToken, session);

        return ResponseEntity.ok().build();
    }
}
