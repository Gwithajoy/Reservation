package com.zerobase.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
    @NotNull
    private Long storeId;

    @NotNull
    private int rating;

    @NotNull
    @Size(min = 1, max = 1000)
    private String content;
}
