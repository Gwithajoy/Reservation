package com.zerobase.reservation.serviceTest;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.dto.request.MemberRegisterRequest;
import com.zerobase.reservation.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void register_success() {
        // given
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setName("Test User");
        request.setPhone("123-4567");
        request.setRole(Role.USER);

        // 이메일 중복 확인: 없다고 가정
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        // 비밀번호 암호화
        when(passwordEncoder.encode("password")).thenReturn("encryptedPassword");
        // 저장 시 ID 부여
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member m = invocation.getArgument(0);
            m.setId(1L);
            return m;
        });

        // when
        Member savedMember = memberService.register(request);

        // then
        assertNotNull(savedMember.getId());
        assertEquals("encryptedPassword", savedMember.getPassword());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    public void register_duplicateEmail_throwsException() {
        // given
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setName("Test User");
        request.setPhone("123-4567");
        request.setRole(Role.USER);

        Member existingMember = new Member();
        existingMember.setId(1L);
        existingMember.setEmail("test@example.com");
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingMember));

        // then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            memberService.register(request);
        });
        assertEquals("이미 사용 중인 이메일입니다.", thrown.getMessage());
    }
}
