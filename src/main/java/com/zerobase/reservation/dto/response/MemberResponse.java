package com.zerobase.reservation.dto.response;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import lombok.Getter;

/**
 * 회원 가입 또는 조회 시 클라이언트에 전달할 회원 정보 응답 DTO입니다.
 */
@Getter
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;

    /**
     * Member 엔티티 객체를 기반으로 DTO를 생성합니다.
     * @param member Member 엔티티 객체
     */
    public MemberResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.phone = member.getPhone();
        this.role = member.getRole();
    }
}
