package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.enums.ReservationStatus;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.ReviewRequest;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.ReviewRepository;
import com.zerobase.reservation.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    /**
     * 리뷰 작성: 예약 완료된 사용자만 작성 가능
     */
    public Review createReview(ReviewRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        // 예약 이력 검증: 해당 매장에서 COMPLETED 상태의 예약이 있는지 확인
        boolean hasCompletedReservation = reservationRepository.existsByStoreIdAndMemberIdAndStatus(
                request.getStoreId(), memberId, ReservationStatus.COMPLETED);
        if (!hasCompletedReservation) {
            throw new RuntimeException("예약 이력이 없어서 리뷰 작성이 불가합니다.");
        }
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("매장이 존재하지 않습니다."));
        Review review = Review.builder()
                .store(store)
                .member(member)
                .rating(request.getRating())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();
        return reviewRepository.save(review);
    }

    /**
     * 리뷰 수정: 작성자만 수정 가능
     */
    public Review updateReview(Long reviewId, ReviewRequest request, Long memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));
        if (!review.getMember().getId().equals(memberId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setUpdatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    /**
     * 리뷰 삭제: 작성자 또는 매장 관리자(점장)만 삭제 가능
     */
    public void deleteReview(Long reviewId, Long memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));
        boolean isWriter = review.getMember().getId().equals(memberId);
        boolean isStoreOwner = review.getStore().getOwner().getId().equals(memberId);
        if (!isWriter && !isStoreOwner) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        reviewRepository.delete(review);
    }
}
