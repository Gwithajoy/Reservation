/**
 * 로그인 요청 시 사용하는 DTO
 * 사용자가 로그인 시 이메일과 비밀번호를 전달받으며, 빈 값이 아닌지 검증합니다.
*/

package com.zerobase.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
