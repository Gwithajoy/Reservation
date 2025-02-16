package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Reservation;
import com.zerobase.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByStoreIdAndMemberIdAndStatus(Long storeId, Long memberId, ReservationStatus status);
}
