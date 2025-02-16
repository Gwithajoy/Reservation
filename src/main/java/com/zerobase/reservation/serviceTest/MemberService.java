package com.zerobase.reservation.serviceTest;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.dto.request.MemberRegisterRequest;
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
     * 회원가입 처리 메서드
     *
     * 클라이언트로부터 회원가입 요청 정보를 받아, 이메일 중복 체크를 진행한 후
     * 비밀번호를 암호화하여 회원 정보를 저장합니다.
     *
     * @param request: 회원가입 요청 DTO (email, password, name, phone, role)
     * @return 저장된 Member 엔티티
     * @throws RuntimeException: 이미 사용 중인 이메일인 경우 예외 발생
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
