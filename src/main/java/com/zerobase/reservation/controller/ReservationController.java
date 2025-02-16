package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.dto.request.ReservationRequest;
import com.zerobase.reservation.dto.response.ReservationResponse;
import com.zerobase.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 생성 API (사용자)
     */
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest request,
                                                                 @RequestParam Long memberId) {
        Reservation reservation = reservationService.createReservation(request, memberId);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }

    /**
     * 예약 승인 API (매장 점장)
     */
    @PostMapping("/{reservationId}/approve")
    public ResponseEntity<ReservationResponse> approveReservation(@PathVariable Long reservationId,
                                                                  @RequestParam Long partnerId) {
        Reservation reservation = reservationService.approveReservation(reservationId, partnerId);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }

    /**
     * 예약 거절 API (매장 점장)
     */
    @PostMapping("/{reservationId}/decline")
    public ResponseEntity<ReservationResponse> declineReservation(@PathVariable Long reservationId,
                                                                  @RequestParam Long partnerId) {
        Reservation reservation = reservationService.declineReservation(reservationId, partnerId);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }

    /**
     * 도착 확인 API (키오스크 등)
     */
    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<ReservationResponse> confirmArrival(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.confirmArrival(reservationId);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }
}
