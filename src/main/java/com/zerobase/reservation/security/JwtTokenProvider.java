package com.zerobase.reservation.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 컴포넌트입니다.
 */
@Component
public class JwtTokenProvider {
    // JWT 암호화에 사용될 비밀 키
    private final String JWT_SECRET = "키값";
    // JWT 만료 시간 (7일)
    private final long JWT_EXPIRATION = 604800000L; // 7일

    /**
     * 인증 정보를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param authentication: 사용자 인증 정보
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(Authentication authentication) {
        // 인증 객체에서 사용자 이메일(식별자)를 가져옴
        String email = authentication.getName();
        // 현재 시간을 기준으로 토큰 발급 시간 설정
        Date now = new Date();
        // 만료 시간을 현재 시간에 JWT_EXPIRATION을 더해 설정
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        // JWT 빌더를 사용하여 토큰 생성 후 반환
        return Jwts.builder()
                .setSubject(email)         // 토큰의 주체(subject)를 사용자 이메일로 설정
                .setIssuedAt(now)          // 토큰 발급 시간 설정
                .setExpiration(expiryDate) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // 비밀 키와 알고리즘으로 서명
                .compact();                // 토큰 생성
    }
}
