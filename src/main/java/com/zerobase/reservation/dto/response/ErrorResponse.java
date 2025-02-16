package com.zerobase.reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 호출 시 에러 상황에 대한 응답 데이터를 담는 DTO입니다.
 * status: HTTP 상태 코드, message: 에러 메시지를 포함합니다.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
}
