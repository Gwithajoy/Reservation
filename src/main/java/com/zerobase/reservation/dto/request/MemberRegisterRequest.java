/**
 * 회원 가입 요청 데이터를 담는 DTO
 * 클라이언트가 회원 가입시 필요한 정보를 서버로 전송할 때 사용합니다.
 */
package com.zerobase.reservation.dto.request;

import com.zerobase.reservation.enums.Role;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
    private Role role;
}
