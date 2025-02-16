package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.dto.request.LoginRequest;
import com.zerobase.reservation.dto.request.MemberRegisterRequest;
import com.zerobase.reservation.dto.response.JwtResponse;
import com.zerobase.reservation.dto.response.MemberResponse;
import com.zerobase.reservation.security.JwtTokenProvider;
import com.zerobase.reservation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입 API 엔드포인트
     */
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberService.register(request);
        return ResponseEntity.ok(new MemberResponse(member));
    }

    /**
     * 로그인 API 엔드포인트
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
