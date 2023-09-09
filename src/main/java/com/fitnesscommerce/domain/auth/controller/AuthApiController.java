package com.fitnesscommerce.domain.auth.controller;

import com.fitnesscommerce.domain.auth.service.AuthService;
import com.fitnesscommerce.domain.auth.dto.request.LoginRequest;
import com.fitnesscommerce.domain.auth.dto.response.JwtResponse;
import com.fitnesscommerce.domain.auth.dto.response.RefreshResponse;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "이메일 없음"),
            @ApiResponse(responseCode = "400", description = "비밀번호 틀림")
    })
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

    @Operation(summary = "토큰 재발급", description = "새로운 accessToken 발급", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "회원 인증 필요")
    })
    @GetMapping("/api/auth/token-refresh") //리프레시 토큰을 보내주면(새로운 액세스 토큰 발급 위함) -> 액세스 토큰이 만료되기 전에 요청
    public ResponseEntity<RefreshResponse> issueNewToken(@CookieValue("refreshToken") String refreshToken) {

        String newAccessToken = authService.validate(refreshToken);

        RefreshResponse response = new RefreshResponse(newAccessToken);

        return ResponseEntity.ok().body(response);

    }

    @Operation(summary = "로그아웃", description = "로그아웃 API", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "회원 인증 필요")
    })
    @PostMapping("/api/auth/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String accessToken,
                                 @CookieValue("refreshToken") String refreshToken,
                                 MemberSession session) {

        authService.expireToken(accessToken, refreshToken, session);

        return ResponseEntity.ok().build();
    }
}
