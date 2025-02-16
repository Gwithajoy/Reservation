package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.dto.request.StoreRequest;
import com.zerobase.reservation.repository.MemberRepository;
import com.zerobase.reservation.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    /**
     * 매장 등록: 파트너 회원만 매장을 등록할 수 있음
     */
    public Store registerStore(StoreRequest request, Long partnerId) {
        Member partner = memberRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("파트너 회원이 존재하지 않습니다."));
        if (!partner.getRole().equals(Role.PARTNER)) {
            throw new RuntimeException("파트너 회원만 매장을 등록할 수 있습니다.");
        }
        Store store = Store.builder()
                .storeName(request.getStoreName())
                .description(request.getDescription())
                .location(request.getLocation())
                .owner(partner)
                .createdAt(LocalDateTime.now())
                .build();
        return storeRepository.save(store);
    }

    /**
     * 매장 수정: 등록자만 수정 가능
     */
    public Store updateStore(Long storeId, StoreRequest request, Long partnerId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장이 존재하지 않습니다."));
        if (!store.getOwner().getId().equals(partnerId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
        store.setStoreName(request.getStoreName());
        store.setDescription(request.getDescription());
        store.setLocation(request.getLocation());
        store.setUpdatedAt(LocalDateTime.now());
        return storeRepository.save(store);
    }

    /**
     * 매장 삭제: 등록자만 삭제 가능
     */
    public void deleteStore(Long storeId, Long partnerId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장이 존재하지 않습니다."));
        if (!store.getOwner().getId().equals(partnerId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        storeRepository.delete(store);
    }

    /**
     * 매장 상세 조회
     */
    public Store getStoreDetails(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장이 존재하지 않습니다."));
    }
}
