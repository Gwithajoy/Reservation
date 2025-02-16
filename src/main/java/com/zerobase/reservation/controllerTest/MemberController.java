package com.zerobase.reservation.controllerTest;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.dto.request.LoginRequest;
import com.zerobase.reservation.dto.request.MemberRegisterRequest;
import com.zerobase.reservation.dto.response.JwtResponse;
import com.zerobase.reservation.dto.response.MemberResponse;
import com.zerobase.reservation.security.JwtTokenProvider;
import com.zerobase.reservation.serviceTest.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * MemberController는 회원 가입 및 로그인 관련 API 엔드포인트를 제공합니다.
 * 회원 가입 시 입력받은 정보를 바탕으로 신규 회원을 등록하며, 로그인 시 JWT 토큰을 발급합니다.
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입 API
     *
     * 사용자가 회원가입 요청 시, 입력한 회원 정보를 기반으로 신규 회원을 등록합니다.
     * 회원 등록에 성공하면, 등록된 회원의 상세 정보(예: 이메일, 이름 등)를 응답으로 반환합니다.
     *
     * @param request 회원 가입에 필요한 정보(이메일, 비밀번호, 이름 등)를 담은 요청 객체
     * @return 등록된 회원 정보를 담은 MemberResponse 객체
     */
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@RequestBody @Valid MemberRegisterRequest request) {
        Member member = memberService.register(request);
        return ResponseEntity.ok(new MemberResponse(member));
    }

    /**
     * 로그인 API
     *
     * 사용자가 로그인 요청 시, 입력한 이메일과 비밀번호를 기반으로 인증을 수행합니다.
     * 인증이 성공하면, JWT 토큰을 생성하여 클라이언트에 반환합니다.
     *
     * @param request 로그인 요청에 필요한 정보(이메일, 비밀번호)를 담은 객체
     * @return 생성된 JWT 토큰을 포함한 JwtResponse 객체
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        // 이메일과 비밀번호를 사용하여 인증 토큰 생성 및 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        // 인증 결과를 SecurityContext에 저장 (세션 관리 등에서 사용)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 인증 정보를 기반으로 JWT 토큰을 생성
        String token = jwtTokenProvider.generateToken(authentication);
        // 생성된 JWT 토큰을 응답으로 반환
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
