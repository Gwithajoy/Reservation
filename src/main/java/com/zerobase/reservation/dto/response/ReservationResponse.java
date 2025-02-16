package com.zerobase.reservation.dto.response;

import com.zerobase.reservation.domain.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationResponse {
    private Long id;
    private Long storeId;
    private Long memberId;
    private LocalDateTime reservationDateTime;
    private String status;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.storeId = reservation.getStore().getId();
        this.memberId = reservation.getMember().getId();
        this.reservationDateTime = reservation.getReservationDateTime();
        this.status = reservation.getStatus().name();
    }
}
