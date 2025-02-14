package com.zerobase.reservation.repository;

import com.zerobase.reservation.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public class MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
