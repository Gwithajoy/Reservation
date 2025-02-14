package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
