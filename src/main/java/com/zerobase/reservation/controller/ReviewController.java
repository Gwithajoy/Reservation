package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.dto.request.ReviewRequest;
import com.zerobase.reservation.dto.response.ReviewResponse;
import com.zerobase.reservation.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성 API (예약 완료한 사용자)
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody @Valid ReviewRequest request,
                                                       @RequestParam Long memberId) {
        Review review = reviewService.createReview(request, memberId);
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    /**
     * 리뷰 수정 API (작성자만)
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long reviewId,
                                                       @RequestBody @Valid ReviewRequest request,
                                                       @RequestParam Long memberId) {
        Review review = reviewService.updateReview(reviewId, request, memberId);
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    /**
     * 리뷰 삭제 API (작성자 또는 매장 관리자)
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId,
                                          @RequestParam Long memberId) {
        reviewService.deleteReview(reviewId, memberId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }
}
