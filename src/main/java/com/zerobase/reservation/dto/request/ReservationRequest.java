/**
 * 예약 요청 시 사용하는 DTO
 * 사용자가 예약을 생성할 때 매장 ID와 예약 일시를 전달받으며, null 값이 아닌지 검증합니다.
 */

package com.zerobase.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {
    @NotNull(message = "매장 ID는 필수 입력 값입니다.")
    private Long storeId;

    @NotNull(message = "예약 일시는 필수 입력 값입니다.")
    private LocalDateTime reservationDateTime;
}
