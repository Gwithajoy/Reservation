package com.zerobase.reservation.serviceTest;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.enums.ReservationStatus;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.ReservationRequest;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.ReservationRepository;
import com.zerobase.reservation.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    /**
     * 예약 생성 메서드
     *
     * 사용자가 특정 매장에 대해 예약 요청을 할 때 호출됩니다.
     * 회원과 매장 정보를 조회한 후 예약 상태를 REQUESTED로 설정하고 예약 정보를 저장합니다
     *
     * @param request: 예약 요청 DTO (storeId, reservationDateTime)
     * @param memberId: 예약을 요청하는 회원의 ID
     * @return 저장된 Reservation 엔티티
     */
    public Reservation createReservation(ReservationRequest request, Long memberId) {
        Member user = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("매장이 존재하지 않습니다."));
        // 예약 가능 여부, 중복 예약 등 추가 검증 로직이 필요할 수 있습니다.
        Reservation reservation = Reservation.builder()
                .store(store)
                .member(user)
                .reservationDateTime(request.getReservationDateTime())
                .status(ReservationStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();
        return reservationRepository.save(reservation);
    }

    /**
     * 예약 승인 메서드
     *
     * 매장 점장이 예약 요청을 승인할 때 호출됩니다.
     * 예약 정보를 조회하여 해당 예약의 매장 소유주와 요청한 파트너의 ID를 비교한 후
     * 예약 상태를 APPROVED로 변경하고 업데이트 시간을 기록합니다.
     *
     * @param reservationId: 승인할 예약의 ID
     * @param partnerId: 예약 요청을 승인하는 파트너(매장 점장)의 ID
     * @return 업데이트된 Reservation 엔티티
     * @throws RuntimeException: 예약이 존재하지 않거나 승인 권한이 없을 경우 예외 발생
     */
    public Reservation approveReservation(Long reservationId, Long partnerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다."));
        if (!reservation.getStore().getOwner().getId().equals(partnerId)) {
            throw new RuntimeException("승인 권한이 없습니다.");
        }
        reservation.setStatus(ReservationStatus.APPROVED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    /**
     * 예약 거절 메서드
     *
     * 매장 점장이 예약 요청을 거절할 때 호출됩니다.
     * 예약 정보를 조회한 후 예약 상태를 REJECTED로 변경하고 업데이트 시간을 기록합니다.
     *
     * @param reservationId: 거절할 예약의 ID
     * @param partnerId: 예약 요청을 거절하는 파트너(매장 점장)의 ID
     * @return 업데이트된 Reservation 엔티티
     * @throws RuntimeException: 예약이 존재하지 않거나 거절 권한이 없을 경우 예외 발생
     */
    public Reservation declineReservation(Long reservationId, Long partnerId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다."));
        if (!reservation.getStore().getOwner().getId().equals(partnerId)) {
            throw new RuntimeException("거절 권한이 없습니다.");
        }
        reservation.setStatus(ReservationStatus.REJECTED);
        reservation.setUpdatedAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    /**
     * 도착 확인 메서드
     *
     * 예약 시간이 기준 10분 전후인 경우에 방문 확인 처리를 하여 예약 상태를 COMPLETED로 변경합니다.
     *
     * @param reservationId: 방문 확인할 예약의 ID
     * @return 업데이트된 Reservation 엔티티
     * @throws RuntimeException: 예약 시간이 도착 확인 가능 시간 범위를 벗어난 경우 예외 발생
     */
    public Reservation confirmArrival(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다."));
        LocalDateTime now = LocalDateTime.now();
        // 예약시간 기준 10분 전부터 10분 후까지 도착 확인 허용
        if (now.isAfter(reservation.getReservationDateTime().minusMinutes(10)) &&
                now.isBefore(reservation.getReservationDateTime().plusMinutes(10))) {
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservation.setUpdatedAt(now);
            return reservationRepository.save(reservation);
        } else {
            throw new RuntimeException("도착 확인 시간이 유효하지 않습니다.");
        }
    }
}
