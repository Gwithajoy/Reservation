package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.dto.request.MemberRegisterRequest;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리: 중복 이메일 체크, 비밀번호 암호화 후 회원 저장
     *
     * @param request 회원가입 요청 DTO (email, password, name, phone, role)
     * @return 저장된 Member 엔티티
     */
    public Member register(MemberRegisterRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhone())
                .role(request.getRole())
                .build();
        return memberRepository.save(member);
    }
}

