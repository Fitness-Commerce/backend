package com.fitnesscommerce.global.config;

import com.fitnesscommerce.domain.auth.exception.Unauthorized;
import com.fitnesscommerce.global.config.data.MemberSession;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final AppConfig appConfig;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberSession.class);
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String token = webRequest.getHeader("Authorization");// 토큰이 null이면 500뜸
        String jws = token.substring(7);

        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(appConfig.getJwtKey())
                    .build()
                    .parseClaimsJws(jws);
            String memberId = claims.getBody().getSubject();

            if (isTokenBlacklisted(jws, memberId)) {
                throw new Unauthorized();
            }

            return new MemberSession(Long.parseLong(memberId));
        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }

    private boolean isTokenBlacklisted(String token, String memberId) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String blackListedToken = valueOperations.get("blacklist-accessToken" + memberId);
        //만약 유효기간이 살아있는 액세스 토큰이 redis에 있다면 요청이 거절됌 - ex) 로그아웃 후 블랙리스트로 이동 된 액세스토큰으로 접근

        return blackListedToken != null && blackListedToken.equals(token);
    }
}
