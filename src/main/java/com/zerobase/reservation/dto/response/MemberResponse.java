package com.zerobase.reservation.dto.response;

import com.zerobase.reservation.domain.Member;
import com.zerobase.reservation.enums.Role;
import lombok.Getter;

@Getter
public class MemberResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;

    /**
     * Member 엔티티를 받아서 MemberResponse DTO로 변환하는 생성자
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
