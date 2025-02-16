package com.zerobase.reservation.serviceTest;

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
     * 리뷰 작성 메서드
     *
     * 예약 이력이 COMPLETED 상태인 경우에만 사용자가 해당 매장에 대한 리뷰를 작성할 수 있습니다.
     * 해당 매장 및 회원 정보를 조회하고, 리뷰 정보를 생성하여 저장합니다.
     *
     * @param request: 리뷰 작성 요청 DTO (storeId, rating, content)
     * @param memberId: 리뷰를 작성하는 회원의 ID
     * @return 저장된 Review 엔티티
     * @throws RuntimeException 예약 이력이 없는 경우 예외 발생
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
     * 리뷰 수정 메서드
     *
     * 리뷰 작성자만 리뷰를 수정할 수 있습니다.
     * 리뷰를 조회한 후, 작성자가 일치하면 평점 및 내용을 업데이트합니다.
     *
     * @param reviewId: 수정할 리뷰의 ID
     * @param request: 수정 요청 DTO (storeId, rating, content)
     * @param memberId: 리뷰 작성자의 ID
     * @return 업데이트된 Review 엔티티
     * @throws RuntimeException 리뷰가 존재하지 않거나 수정 권한이 없는 경우 예외 발생
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
     * 리뷰 삭제 메서드
     *
     * 리뷰 삭제는 리뷰 작성자 또는 해당 매장의 소유자(점장)만 수행할 수 있습니다.
     * 삭제 권한이 없는 경우 예외를 발생시킵니다.
     *
     * @param reviewId: 삭제할 리뷰의 ID
     * @param memberId: 요청한 회원의 ID
     * @throws RuntimeException 삭제 권한이 없는 경우 예외 발생
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
