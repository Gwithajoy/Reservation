package com.zerobase.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {
    @NotBlank
    private String storeName;

    @NotBlank
    private String location;

    private String description;
}
