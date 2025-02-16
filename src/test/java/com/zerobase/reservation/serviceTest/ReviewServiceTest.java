package com.zerobase.reservation.serviceTest;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.enums.ReservationStatus;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.ReviewRequest;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.ReviewRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReviewService reviewService;

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
                .storeName("Review Store")
                .build();
    }

    @Test
    public void createReview_success() {
        // 가정: 해당 매장에서 COMPLETED 상태의 예약 이력이 존재
        when(reservationRepository.existsByStoreIdAndMemberIdAndStatus(10L, 1L, ReservationStatus.COMPLETED))
                .thenReturn(true);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(10L)).thenReturn(Optional.of(store));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review r = invocation.getArgument(0);
            r.setId(100L);
            return r;
        });

        ReviewRequest request = new ReviewRequest();
        request.setStoreId(10L);
        request.setRating(5);
        request.setContent("Excellent service!");

        Review review = reviewService.createReview(request, 1L);
        assertNotNull(review.getId());
        assertEquals(5, review.getRating());
    }

    @Test
    public void updateReview_success() {
        Review review = Review.builder()
                .id(200L)
                .store(store)
                .member(user)
                .rating(3)
                .content("Good")
                .createdAt(LocalDateTime.now())
                .build();
        when(reviewRepository.findById(200L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewRequest request = new ReviewRequest();
        request.setStoreId(10L);
        request.setRating(4);
        request.setContent("Better now");

        Review updatedReview = reviewService.updateReview(200L, request, 1L);
        assertEquals(4, updatedReview.getRating());
        assertEquals("Better now", updatedReview.getContent());
    }

    @Test
    public void deleteReview_success() {
        Review review = Review.builder()
                .id(300L)
                .store(store)
                .member(user)
                .rating(3)
                .content("Average")
                .build();
        when(reviewRepository.findById(300L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(300L, 1L);
        verify(reviewRepository, times(1)).delete(review);
    }
}
