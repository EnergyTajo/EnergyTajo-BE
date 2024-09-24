package com.energy.tajo.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 Bad Request
    INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    INVALID_REQUEST(BAD_REQUEST, "잘못된 요청입니다."),

    // 500 서버 에러
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),
    PASSWORD_ENCRYPTION_ERROR(INTERNAL_SERVER_ERROR, "암호화에 문제가 발생했습니다. 관리자에게 문의하세요."),
    SHA256_ALGORITHM_NOT_FOUND(INTERNAL_SERVER_ERROR, "SHA-256 알고리즘을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}