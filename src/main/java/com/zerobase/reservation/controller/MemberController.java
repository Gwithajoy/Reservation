package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.dto.request.MemberRegisterRequest;
import com.zerobase.reservation.dto.response.MemberResponse;
import com.zerobase.reservation.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> register(@RequestBody MemberRegisterRequest request) {
        Member member = memberService.register(request);
        return ResponseEntity.ok(new MemberResponse(member));
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {}
}
