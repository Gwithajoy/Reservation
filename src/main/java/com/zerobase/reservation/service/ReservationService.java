package com.zerobase.reservation.service;

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
     * 예약 생성: 사용자가 매장을 예약 요청
     */
    public Reservation createReservation(ReservationRequest request, Long memberId) {
        Member user = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new RuntimeException("매장이 존재하지 않습니다."));
        // 예약 가능 여부, 중복 예약 등 추가 검증 필요
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
     * 예약 승인: 매장 점장이 예약 요청을 승인
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
     * 예약 거절: 매장 점장이 예약 요청을 거절
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
     * 도착 확인: 예약 시간 기준 10분 전후에 방문 확인 처리
     */
    public Reservation confirmArrival(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약이 존재하지 않습니다."));
        LocalDateTime now = LocalDateTime.now();
        // 예약시간 기준 10분 전부터 10분 후까지 방문 확인 허용
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
