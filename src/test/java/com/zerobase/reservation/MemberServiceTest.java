package com.zerobase.reservation;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.dto.request.MemberRegisterRequest;
import com.zerobase.reservation.exception.CustomException;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    void register_success() {
        // given
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setName("홍길동");
        request.setPhone("010-1234-5678");
        request.setRole(Role.USER);

        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encryptedPassword");
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
    void register_duplicateEmail() {
        // given
        MemberRegisterRequest request = new MemberRegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setName("홍길동");
        request.setPhone("010-1234-5678");
        request.setRole(Role.USER);

        Member existingMember = Member.builder().id(1L).email("test@example.com").build();
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existingMember));

        // then
        assertThrows(CustomException.class, () -> memberService.register(request));
    }
}
