package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.StoreRequest;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private StoreService storeService;

    private Member partner;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        partner = Member.builder()
                .id(1L)
                .email("partner@example.com")
                .role(Role.PARTNER)
                .build();
    }

    @Test
    public void registerStore_success() {
        // given
        StoreRequest request = new StoreRequest();
        request.setStoreName("Test Store");
        request.setDescription("Description");
        request.setLocation("Location");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(partner));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> {
            Store store = invocation.getArgument(0);
            store.setId(100L);
            return store;
        });

        // when
        Store savedStore = storeService.registerStore(request, 1L);

        // then
        assertNotNull(savedStore.getId());
        assertEquals("Test Store", savedStore.getStoreName());
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    public void registerStore_invalidPartner_throwsException() {
        // given
        StoreRequest request = new StoreRequest();
        request.setStoreName("Test Store");
        request.setDescription("Description");
        request.setLocation("Location");

        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            storeService.registerStore(request, 1L);
        });
        assertEquals("파트너 회원이 존재하지 않습니다.", thrown.getMessage());
    }
}
