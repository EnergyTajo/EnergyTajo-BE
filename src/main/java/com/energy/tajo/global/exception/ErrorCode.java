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
    ID_IN_USE(BAD_REQUEST, "사용중인 ID 입니다."),
    INVALID_PHONE_NUMBER(BAD_REQUEST,"전화번호 형식이 올바르지 않습니다."),
    PHONE_NUMBER_NOT_VERIFIED(BAD_REQUEST, "인증 되지 않은 전화번호입니다."),
    INVALID_VERIFICATION_CODE(BAD_REQUEST, "인증 번호가 일치하지 않습니다."),
    VERIFICATION_FAILED(BAD_REQUEST,"인증 번호 검증에 실패했습니다."),
    NO_VERIFICATION_RECORD(BAD_REQUEST,"인증 기록이 존재하지 않습니다."),
    ALREADY_VERIFIED(BAD_REQUEST, "이미 인증되었습니다."),
    TWILIO_ERROR(BAD_REQUEST, "Twilio 서비스 오류가 발생했습니다."),

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
