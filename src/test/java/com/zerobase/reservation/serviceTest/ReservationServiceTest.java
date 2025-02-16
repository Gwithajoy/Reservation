package com.zerobase.reservation.serviceTest;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.enums.ReservationStatus;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.ReservationRequest;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.ReservationRepository;
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

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Member user;
    private Store store;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = Member.builder()
                .id(1L)
                .email("user@example.com")
                .role(com.zerobase.reservation.enums.Role.USER)
                .build();
        store = Store.builder()
                .id(10L)
                .storeName("Test Store")
                .build();
    }

    @Test
    public void createReservation_success() {
        // given
        ReservationRequest request = new ReservationRequest();
        request.setStoreId(10L);
        request.setReservationDateTime(LocalDateTime.now().plusDays(1));

        when(memberRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(10L)).thenReturn(Optional.of(store));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation r = invocation.getArgument(0);
            r.setId(100L);
            return r;
        });

        // when
        Reservation reservation = reservationService.createReservation(request, 1L);

        // then
        assertNotNull(reservation.getId());
        assertEquals(ReservationStatus.REQUESTED, reservation.getStatus());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void approveReservation_success() {
        // given
        Reservation reservation = Reservation.builder()
                .id(200L)
                .store(store)
                .createdAt(LocalDateTime.now())
                .build();
        // store owner가 파트너인 경우: partner ID = 2L
        Member partner = Member.builder()
                .id(2L)
                .email("partner@example.com")
                .role(Role.PARTNER)
                .build();
        store.setOwner(partner);

        when(reservationRepository.findById(200L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Reservation approved = reservationService.approveReservation(200L, 2L);

        // then
        assertNotNull(approved); // approved가 null이 아님을 확인
        assertEquals(ReservationStatus.APPROVED, approved.getStatus());
    }

    @Test
    public void declineReservation_success() {
        Reservation reservation = Reservation.builder()
                .id(300L)
                .store(store)
                .createdAt(LocalDateTime.now())
                .build();
        Member partner = Member.builder()
                .id(2L)
                .email("partner@example.com")
                .role(Role.PARTNER)
                .build();
        store.setOwner(partner);

        when(reservationRepository.findById(300L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Reservation declined = reservationService.declineReservation(300L, 2L);

        // then
        assertNotNull(declined);
        assertEquals(ReservationStatus.REJECTED, declined.getStatus());
    }


    @Test
    public void confirmArrival_success() {
        // given: 예약 시간이 현재로부터 5분 후
        Reservation reservation = Reservation.builder()
                .id(400L)
                .store(store)
                .reservationDateTime(LocalDateTime.now().plusMinutes(5))
                .status(ReservationStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        when(reservationRepository.findById(400L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // when
        Reservation confirmed = reservationService.confirmArrival(400L);

        // then
        assertEquals(ReservationStatus.COMPLETED, confirmed.getStatus());
    }
}
