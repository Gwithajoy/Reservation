package com.zerobase.reservation.dto.response;

import com.zerobase.reservation.domain.Store;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreResponse {
    private Long id;



    private String store_name;
    private String location;
    private String description;
    private Long owner_id;

    public StoreResponse(Store store) {
        this.id = store.getId();
        this.store_name = store.getStoreName();
        this.location = store.getLocation();
        this.description = store.getDescription();
        this.owner_id = store.getOwner().getId();
    }
}
