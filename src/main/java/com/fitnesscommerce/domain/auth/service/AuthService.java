package com.fitnesscommerce.domain.auth.service;

import com.fitnesscommerce.domain.auth.exception.InvalidRefreshToken;
import com.fitnesscommerce.domain.member.crypto.PasswordEncoder;
import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.auth.dto.request.LoginRequest;
import com.fitnesscommerce.domain.member.exception.EmailNotFound;
import com.fitnesscommerce.domain.member.exception.InvalidPassword;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.global.config.AppConfig;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppConfig appConfig;
    private final RedisTemplate<String, String> redisTemplate;

    private final Map<String, Integer> logoutCounts;

    private Date accessTime;
    private Date refreshTime;

    public static long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000; // 30분
    public static long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7일

    @Transactional
    public String[] login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(EmailNotFound::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new InvalidPassword();
        }

        long curTime = System.currentTimeMillis();

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        accessTime = new Date(curTime + ACCESS_TOKEN_EXPIRATION_TIME);
        refreshTime = new Date(curTime + REFRESH_TOKEN_EXPIRATION_TIME);



        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim("tokenType", "access")
                .claim("memberId", member.getId())
                .signWith(key)
                .setExpiration(accessTime)
                .setIssuedAt(new Date(curTime))
                .compact();

        String refreshTokenId = generateRandomToken();

        String refreshToken = Jwts.builder()
                //.setSubject(String.valueOf(member.getId())) // -> refresh token으로도 접근이 가능하게 함 (비추)
                .claim("tokenType", "refresh")
                .claim("refreshTokenId", refreshTokenId)
                .claim("memberId", String.valueOf(member.getId()))
                .signWith(key)
                .setExpiration(refreshTime)
                .setIssuedAt(new Date(curTime))
                .compact();

        //refreshtoken redis에 저장하기
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expiration = Duration.ofDays(7);
        valueOperations.set(String.valueOf(member.getId()), refreshToken, expiration);

        if (logoutCounts.containsKey(String.valueOf(member.getId()))) {
            log.info("{}번 회원이 로그인 하였습니다. 로그인 시간 = {}", member.getId(), curTime);
        } else {
            logoutCounts.put(String.valueOf(member.getId()), 0);//첫 로그인
            log.info("{}번 회원이 로그인 하였습니다. 로그인 시간 = {}", member.getId(), curTime);
        }


        log.info("{}번 회원의 accessToken 만료시간은 {} 입니다.", member.getId(), accessTime);
        log.info("{}번 회원의 refreshToken 만료시간은 {} 입니다.", member.getId(), refreshTime);

        return new String[]{accessToken, refreshToken};
    }

    //액세스 만료전에 요청하지 말것 - 클라이언트
    public String validate(String refreshToken) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken);

        String memberId = claims.getBody().get("memberId", String.class);

        String redisRefreshToken = redisTemplate.opsForValue().get(memberId);

        //refresh토큰이 유효하면
        if (refreshToken.equals(redisRefreshToken)) {

            long curTime = System.currentTimeMillis();

            String accessTokenId = generateRandomToken();

            String newAccessToken = Jwts.builder()
                    //.setSubject(String.valueOf(memberId))
                    .claim("tokenType", "access")
                    .claim("accessTokenId", accessTokenId)
                    .signWith(key)
                    .setExpiration(new Date(curTime + ACCESS_TOKEN_EXPIRATION_TIME))
                    .setIssuedAt(new Date(curTime))
                    .compact();

            long refreshTime = System.currentTimeMillis();


            accessTime = new Date(refreshTime + ACCESS_TOKEN_EXPIRATION_TIME);

            log.info("{}회원의 accessToken을 재발급하였습니다.", memberId);


            return newAccessToken;
        } else {//refreshtoken이 만료되었다면
            throw new InvalidRefreshToken();
        }

    }

    public void expireToken(String accessToken, String refreshToken, MemberSession session) {

        if (logoutCounts.containsKey(String.valueOf(session.id))) {
            Integer currentValue = logoutCounts.get(String.valueOf(session.id));
            logoutCounts.put(String.valueOf(session.id), currentValue + 1);
        }

        long curTime = System.currentTimeMillis();

        Duration expirationAccess = Duration.between(new Date(curTime).toInstant(), accessTime.toInstant());
        Duration expirationRefresh = Duration.between(new Date(curTime).toInstant(), refreshTime.toInstant());

        Integer value = logoutCounts.get(String.valueOf(session.id));
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("blacklist-accessToken" + session.id + "-" + value, accessToken, expirationAccess);
        valueOperations.set("blacklist-refreshToken"+ session.id + "-" + value, refreshToken, expirationRefresh);

        log.info("{}회원이 로그아웃 하였습니다.", session.id);
    }

    public static String generateRandomToken() {
        return UUID.randomUUID().toString();

    }



}
