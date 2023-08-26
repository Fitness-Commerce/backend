package com.fitnesscommerce.domain.member.service;

import com.fitnesscommerce.domain.auth.dto.request.LoginRequest;
import com.fitnesscommerce.domain.member.crypto.PasswordEncoder;
import com.fitnesscommerce.domain.member.domain.Address;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.dto.request.MemberJoinRequest;
import com.fitnesscommerce.domain.member.exception.*;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.AppConfig;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;

    public static long ACCESS_TOKEN_EXPIRATION_TIME = 1 * 60 * 1000; // 30분
    public static long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7일

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

    @Transactional
    public String[] login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(EmailNotFound::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidPassword();
        }

        long curTime = System.currentTimeMillis();

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());
        System.out.println(new Date(curTime + ACCESS_TOKEN_EXPIRATION_TIME));

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("tokenType", "access")
                .signWith(key)
                .setExpiration(new Date(curTime + ACCESS_TOKEN_EXPIRATION_TIME))
                .setIssuedAt(new Date(curTime))
                .compact();

        System.out.println();

        String refreshTokenId = generateRandomToken();

        String refreshToken = Jwts.builder()
                //.setSubject(String.valueOf(member.getId())) // -> refresh token으로도 접근이 가능하게 함 (비추)
                .claim("tokenType", "refresh")
                .claim("refreshTokenId", refreshTokenId)
                .signWith(key)
                .setExpiration(new Date(curTime + REFRESH_TOKEN_EXPIRATION_TIME))
                .setIssuedAt(new Date(curTime))
                .compact();

        member.saveRefreshToken(refreshToken);

        return new String[]{accessToken, refreshToken};
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


    public String validate(String refreshToken, MemberSession session) {

        Member member = memberRepository.findById(session.id).orElseThrow(IdNotFound::new);

        if (member.getRefreshToken().equals(refreshToken)) {

            SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

            long curTime = System.currentTimeMillis();

            String accessTokenId = generateRandomToken();
            System.out.println(new Date(curTime + ACCESS_TOKEN_EXPIRATION_TIME));

            String newAccessToken = Jwts.builder()
                    .setSubject(String.valueOf(session.id))
                    .claim("tokenType", "access")
                    .claim("accessTokenId", accessTokenId)
                    .signWith(key)
                    .setExpiration(new Date(curTime + ACCESS_TOKEN_EXPIRATION_TIME))
                    .setIssuedAt(new Date(curTime))
                    .compact();

            return newAccessToken;
        } else {
            throw new IllegalArgumentException();//todo 수정해야함
        }
    }


    public static String generateRandomToken() {
        return UUID.randomUUID().toString();

    }
}
