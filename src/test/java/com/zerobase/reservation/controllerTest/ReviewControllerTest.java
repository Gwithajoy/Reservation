package com.zerobase.reservation.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.ReviewRequest;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.ReviewRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.repository.ReservationRepository;
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
public class ReviewControllerTest {

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

    @Autowired
    private ReviewRepository reviewRepository;

    private Long userId;
    private Long partnerId;
    private Long storeId;

    @BeforeEach
    public void setup() {
        reviewRepository.deleteAll();
        reservationRepository.deleteAll();
        storeRepository.deleteAll();
        memberRepository.deleteAll();

        // 리뷰 작성이 가능한 사용자 (ROLE.USER)
        Member user = Member.builder()
                .email("user@example.com")
                .password("password")
                .name("Review User")
                .role(Role.USER)
                .build();
        user = memberRepository.save(user);
        userId = user.getId();

        // 파트너 생성
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
                .storeName("Review Store")
                .description("Store for reviews")
                .location("Some Location")
                .owner(partner)
                .createdAt(LocalDateTime.now())
                .build();
        store = storeRepository.save(store);
        storeId = store.getId();

        // 예약 완료된 예약 생성 (리뷰 작성 전 조건)
        reservationRepository.save(
                com.zerobase.reservation.domain.Reservation.builder()
                        .store(store)
                        .member(user)
                        .reservationDateTime(LocalDateTime.now().minusDays(1))
                        .status(com.zerobase.reservation.enums.ReservationStatus.COMPLETED)
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .build()
        );
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void createReview_success() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setStoreId(storeId);
        request.setRating(5);
        request.setContent("Excellent service!");

        mockMvc.perform(post("/api/reviews")
                        .param("memberId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating", is(5)))
                .andExpect(jsonPath("$.content", is("Excellent service!")));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void updateReview_success() throws Exception {
        // 먼저 리뷰 생성
        Review review = Review.builder()
                .store(storeRepository.findById(storeId).orElseThrow())
                .member(memberRepository.findById(userId).orElseThrow())
                .rating(4)
                .content("Good")
                .createdAt(LocalDateTime.now())
                .build();
        review = reviewRepository.save(review);

        ReviewRequest updateRequest = new ReviewRequest();
        updateRequest.setStoreId(storeId);
        updateRequest.setRating(5);
        updateRequest.setContent("Excellent!");

        mockMvc.perform(put("/api/reviews/{reviewId}", review.getId())
                        .param("memberId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating", is(5)))
                .andExpect(jsonPath("$.content", is("Excellent!")));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void deleteReview_success() throws Exception {
        // 리뷰 생성
        Review review = Review.builder()
                .store(storeRepository.findById(storeId).orElseThrow())
                .member(memberRepository.findById(userId).orElseThrow())
                .rating(3)
                .content("Average")
                .createdAt(LocalDateTime.now())
                .build();
        review = reviewRepository.save(review);

        mockMvc.perform(delete("/api/reviews/{reviewId}", review.getId())
                        .param("memberId", userId.toString()))
                .andExpect(status().isOk());
    }
}
