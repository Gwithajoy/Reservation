package com.zerobase.reservation.dto.response;

import com.zerobase.reservation.domain.Store;
import lombok.Getter;
import lombok.Setter;

/**
 * 매장 등록, 수정, 조회 등의 API 호출 후 클라이언트에 전달할 매장 정보 응답 DTO입니다.
 */
@Getter
@Setter
public class StoreResponse {
    private Long id;
    private String store_name;
    private String location;
    private String description;
    private Long owner_id;

    /**
     * Store 엔티티 객체를 기반으로 DTO를 생성합니다.
     * @param store Store 엔티티 객체
     */
    public StoreResponse(Store store) {
        this.id = store.getId();
        this.store_name = store.getStoreName();
        this.location = store.getLocation();
        this.description = store.getDescription();
        this.owner_id = store.getOwner().getId();
    }
}
