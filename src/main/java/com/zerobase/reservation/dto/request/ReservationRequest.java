package com.zerobase.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequest {
    @NotNull
    private Long storeId;

    @NotNull
    private LocalDateTime reservationDateTime;
}
