package com.zerobase.reservation.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 성공 후 클라이언트에 전달할 JWT 토큰 응답 DTO입니다.
 */
@Getter
@Setter
public class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }
}
