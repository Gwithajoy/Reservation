package com.zerobase.reservation.serviceTest;

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
     * 매장 등록 메서드
     *
     * 파트너 회원만 매장을 등록할 수 있습니다.
     * 파트너 회원 정보와 요청받은 매장 정보를 바탕으로 새로운 매장을 생성 및 저장합니다.
     *
     * @param request: 매장 등록 요청 DTO (storeName, location, description)
     * @param partnerId: 매장 등록 요청을 하는 파트너 회원의 ID
     * @return 저장된 Store 엔티티
     * @throws RuntimeException 파트너 회원이 존재하지 않거나 권한이 없는 경우 예외 발생
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
     * 매장 수정 메서드
     *
     * 매장 등록자(소유자)만 해당 매장을 수정할 수 있습니다.
     * 요청받은 매장 정보를 바탕으로 매장 정보를 업데이트합니다.
     *
     * @param storeId: 수정할 매장의 ID
     * @param request: 매장 수정 요청 DTO (storeName, location, description)
     * @param partnerId:수정 요청을 하는 파트너 회원의 ID
     * @return 업데이트된 Store 엔티티
     * @throws RuntimeException 매장이 존재하지 않거나 수정 권한이 없는 경우 예외 발생
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
     * 매장 삭제 메서드
     *
     * 매장 등록자(소유자)만 해당 매장을 삭제할 수 있습니다.
     * 삭제 권한이 없는 경우 예외를 발생시킵니다.
     *
     * @param storeId: 삭제할 매장의 ID
     * @param partnerId: 삭제 요청을 하는 파트너 회원의 ID
     * @throws RuntimeException 매장이 존재하지 않거나 삭제 권한이 없는 경우 예외 발생
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
     * 매장 상세 조회 메서드
     *
     * 주어진 매장 ID에 해당하는 매장 정보를 조회하여 반환합니다.
     *
     * @param storeId: 조회할 매장의 ID
     * @return 조회된 Store 엔티티
     * @throws RuntimeException: 매장이 존재하지 않을 경우 예외 발생
     */
    public Store getStoreDetails(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("매장이 존재하지 않습니다."));
    }
}
