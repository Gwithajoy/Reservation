package com.zerobase.reservation.dto.response;

import com.zerobase.reservation.domain.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 예약 생성, 수정, 조회 등의 API 호출 후 클라이언트에 전달할 예약 정보 응답 DTO입니다.
 */
@Getter
@Setter
public class ReservationResponse {
    private Long id;
    private Long storeId;
    private Long memberId;
    private LocalDateTime reservationDateTime;
    private String status;

    /**
     * Reservation 엔티티 객체를 기반으로 DTO를 생성합니다.
     * @param reservation Reservation 엔티티 객체
     */
    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.storeId = reservation.getStore().getId();
        this.memberId = reservation.getMember().getId();
        this.reservationDateTime = reservation.getReservationDateTime();
        this.status = reservation.getStatus().name();
    }
}
