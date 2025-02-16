/**
 * 리뷰 요청 시 사용하는 DTO
 * 사용자가 리뷰를 작성할 때 매장 ID, 평점, 그리고 리뷰 내용을 전달받으며, 각각 null 및 길이 제한 검증을 수행합니다.
 */

package com.zerobase.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    @NotNull(message = "매장 ID는 필수 입력 값입니다.")
    private Long storeId;

    @NotNull(message = "평점은 필수 입력 값입니다.")
    private int rating;

    @NotNull(message = "리뷰 내용은 필수 입력 값입니다.")
    @Size(min = 1, max = 1000, message = "리뷰 내용은 최소 1자에서 최대 1000자까지 입력할 수 있습니다.")
    private String content;
}
