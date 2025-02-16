package com.zerobase.reservation.controllerTest;

import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.dto.request.ReviewRequest;
import com.zerobase.reservation.dto.response.ReviewResponse;
import com.zerobase.reservation.serviceTest.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 리뷰(Review) 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 * 이 컨트롤러는 리뷰 작성, 수정, 삭제 기능을 담당합니다.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성 API
     *
     * 예약 완료한 사용자가 매장 이용 후 리뷰를 작성할 때 사용됩니다.
     * 리뷰 요청 정보에는 매장 ID, 평점, 리뷰 내용 등이 포함됩니다.
     *
     * @param request: 리뷰 작성 정보 (매장 ID, 평점, 내용 등)
     * @param memberId : 리뷰를 작성하는 사용자의 ID (요청 파라미터)
     * @return 작성된 리뷰의 상세 정보가 포함된 응답 객체
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@RequestBody @Valid ReviewRequest request,
                                                       @RequestParam Long memberId) {
        Review review = reviewService.createReview(request, memberId);
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    /**
     * 리뷰 수정 API
     *
     * 리뷰 작성자 본인이 기존에 작성한 리뷰를 수정할 때 사용됩니다.
     * 리뷰 ID와 함께 수정할 리뷰 정보를 전달하면, 해당 리뷰가 업데이트 됩니다.
     *
     * @param reviewId: 수정할 리뷰의 ID (경로 변수)
     * @param request: 수정할 리뷰 정보 (평점, 내용 등)
     * @param memberId: 리뷰를 수정하는 사용자의 ID (요청 파라미터)
     * @return 수정된 리뷰의 상세 정보가 포함된 응답 객체
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long reviewId,
                                                       @RequestBody @Valid ReviewRequest request,
                                                       @RequestParam Long memberId) {
        Review review = reviewService.updateReview(reviewId, request, memberId);
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    /**
     * 리뷰 삭제 API
     *
     * 리뷰 작성자나 매장 관리자가 리뷰를 삭제할 때 사용됩니다.
     * 해당 리뷰의 ID와 삭제를 요청하는 사용자의 ID를 전달받아, 삭제 처리를 수행합니다.
     *
     * @param reviewId: 삭제할 리뷰의 ID (경로 변수)
     * @param memberId: 리뷰 삭제를 요청하는 사용자의 ID (요청 파라미터)
     * @return 삭제 완료 메시지를 반환
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId,
                                          @RequestParam Long memberId) {
        reviewService.deleteReview(reviewId, memberId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }
}
