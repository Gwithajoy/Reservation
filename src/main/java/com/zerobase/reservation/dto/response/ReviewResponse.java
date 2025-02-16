package com.zerobase.reservation.dto.response;

import com.zerobase.reservation.domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponse {
    private Long id;
    private Long storeId;
    private Long memberId;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Review 엔티티 객체를 기반으로 ReviewResponse DTO를 생성하는 생성자입니다.
     * @param review Review 엔티티 객체
     */
    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.storeId = review.getStore().getId();
        this.memberId = review.getMember().getId();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
