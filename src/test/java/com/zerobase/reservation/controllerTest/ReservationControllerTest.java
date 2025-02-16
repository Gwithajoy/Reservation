package com.zerobase.reservation.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.ReservationRequest;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.enums.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Long userId;
    private Long partnerId;
    private Long storeId;

    @BeforeEach
    public void setup() {
        reservationRepository.deleteAll();
        storeRepository.deleteAll();
        memberRepository.deleteAll();

        // 사용자(예약자) 생성 (ROLE.USER)
        Member user = Member.builder()
                .email("user@example.com")
                .password("password")
                .name("User")
                .role(Role.USER)
                .build();
        user = memberRepository.save(user);
        userId = user.getId();

        // 파트너 생성 (ROLE.PARTNER)
        Member partner = Member.builder()
                .email("partner@example.com")
                .password("password")
                .name("Partner")
                .role(Role.PARTNER)
                .build();
        partner = memberRepository.save(partner);
        partnerId = partner.getId();

        // 매장 생성 (파트너 소유)
        Store store = Store.builder()
                .storeName("Test Store")
                .description("Store Description")
                .location("Store Location")
                .owner(partner)
                .createdAt(LocalDateTime.now())
                .build();
        store = storeRepository.save(store);
        storeId = store.getId();
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void createReservation_success() throws Exception {
        ReservationRequest request = new ReservationRequest();
        request.setStoreId(storeId);
        request.setReservationDateTime(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/reservations")
                        .param("memberId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId", is(storeId.intValue())))
                .andExpect(jsonPath("$.status", is("REQUESTED")));
    }

    @Test
    @WithMockUser(username = "partner@example.com", roles = {"PARTNER"})
    public void approveReservation_success() throws Exception {
        // 예약 생성 (초기 상태: REQUESTED)
        Reservation reservation = Reservation.builder()
                .store(storeRepository.findById(storeId).orElseThrow())
                .member(memberRepository.findById(userId).orElseThrow())
                .reservationDateTime(LocalDateTime.now().plusDays(1))
                .status(ReservationStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        reservation = reservationRepository.save(reservation);

        // 파트너가 승인
        mockMvc.perform(post("/api/reservations/{reservationId}/approve", reservation.getId())
                        .param("partnerId", partnerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    @WithMockUser(username = "partner@example.com", roles = {"PARTNER"})
    public void declineReservation_success() throws Exception {
        Reservation reservation = Reservation.builder()
                .store(storeRepository.findById(storeId).orElseThrow())
                .member(memberRepository.findById(userId).orElseThrow())
                .reservationDateTime(LocalDateTime.now().plusDays(1))
                .status(ReservationStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        reservation = reservationRepository.save(reservation);

        // 파트너가 예약 거절
        mockMvc.perform(post("/api/reservations/{reservationId}/decline", reservation.getId())
                        .param("partnerId", partnerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("REJECTED")));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void confirmArrival_success() throws Exception {
        // 예약 시간 5분 후로 설정하여 도착 확인 테스트 (예시)
        Reservation reservation = Reservation.builder()
                .store(storeRepository.findById(storeId).orElseThrow())
                .member(memberRepository.findById(userId).orElseThrow())
                .reservationDateTime(LocalDateTime.now().plusMinutes(5))
                .status(ReservationStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        reservation = reservationRepository.save(reservation);

        mockMvc.perform(post("/api/reservations/{reservationId}/confirm", reservation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")));
    }
}
