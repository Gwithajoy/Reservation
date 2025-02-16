package com.zerobase.reservation.controllerTest;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.dto.request.ReservationRequest;
import com.zerobase.reservation.dto.response.ReservationResponse;
import com.zerobase.reservation.serviceTest.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * 예약(Reservation) 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 * 이 컨트롤러는 예약 생성, 승인, 거절, 도착 확인 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 생성 API

     * 사용자가 예약 요청 정보를 전달하면, 해당 예약을 생성하여 예약 상세 정보를 반환합니다.
     * 예약 요청에는 예약 일시 등의 정보가 포함되며, 예약 생성 후 예약 상태는 'REQUESTED'로 설정됩니다.
     *
     * @param request: 예약 요청 정보 (예약 일시, 매장 정보 등)
     * @param memberId: 예약을 요청하는 사용자 ID (요청 파라미터)
     * @return 생성된 예약의 상세 정보가 포함된 응답 객체
     */
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest request,
                                                                 @RequestParam Long memberId) {
        Reservation reservation = reservationService.createReservation(request, memberId);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }

    /**
     * 예약 승인 API
     * 매장 점장(파트너)이 특정 예약을 승인할 때 호출됩니다.
     * 승인 처리 후 예약 상태는 'APPROVED'로 변경되어 반환됩니다.
     *
     * @param reservationId: 승인할 예약의 ID (경로 변수)
     * @param partnerId: 승인 요청을 하는 파트너(점장)의 ID (요청 파라미터)
     * @return 승인된 예약의 상세 정보가 포함된 응답 객체
     */
    @PostMapping("/{reservationId}/approve")
    public ResponseEntity<ReservationResponse> approveReservation(@PathVariable Long reservationId,
                                                                  @RequestParam Long partnerId) {
        Reservation reservation = reservationService.approveReservation(reservationId, partnerId);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }

    /**
     * 예약 거절 API
     *
     * 매장 점장이 예약 요청을 거절할 때 호출되는 API입니다.
     * 예약 상태는 'REJECTED'로 업데이트 되며, 그 결과가 응답으로 전달됩니다.
     *
     * @param reservationId: 거절할 예약의 ID (경로 변수)
     * @param partnerId: 거절 요청을 하는 파트너(점장)의 ID (요청 파라미터)
     * @return 거절된 예약의 상세 정보가 포함된 응답 객체
     */
    @PostMapping("/{reservationId}/decline")
    public ResponseEntity<ReservationResponse> declineReservation(@PathVariable Long reservationId,
                                                                  @RequestParam Long partnerId) {
        Reservation reservation = reservationService.declineReservation(reservationId, partnerId);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }

    /**
     * 도착 확인 API
     *
     * 예약자가 예약 시간에 도착했음을 확인하기 위해 키오스크 등에서 호출하는 API입니다.
     * 도착 확인이 완료되면, 예약 상태는 'COMPLETED'로 변경되어 반환됩니다.
     *
     * @param reservationId: 도착 확인할 예약의 ID (경로 변수)
     * @return 도착 확인된 예약의 상세 정보가 포함된 응답 객체
     */
    @PostMapping("/{reservationId}/confirm")
    public ResponseEntity<ReservationResponse> confirmArrival(@PathVariable Long reservationId) {
        Reservation reservation = reservationService.confirmArrival(reservationId);
        return ResponseEntity.ok(new ReservationResponse(reservation));
    }
}
