/**
 * 매장 요청 시 사용하는 DTO
 * 매장을 등록 또는 수정할 때 매장 이름, 위치는 필수이고, 설명은 선택적으로 전달받습니다.
 */

package com.zerobase.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {
    @NotBlank(message = "매장 이름은 필수 입력 값입니다.")
    private String storeName;

    @NotBlank(message = "매장 위치는 필수 입력 값입니다.")
    private String location;

    private String description;
}
