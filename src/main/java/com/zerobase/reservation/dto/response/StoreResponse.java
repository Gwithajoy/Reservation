package com.zerobase.reservation.dto.response;

import com.zerobase.reservation.domain.Store;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreResponse {
    private Long id;
    private String storeName;
    private String location;
    private String description;
    private Long ownerId;

    public StoreResponse(Store store) {
        this.id = store.getId();
        this.storeName = store.getStoreName();
        this.location = store.getLocation();
        this.description = store.getDescription();
        this.ownerId = store.getOwner().getId();
    }
}
