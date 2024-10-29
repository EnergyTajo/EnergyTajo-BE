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
    ID_IN_USE(BAD_REQUEST, "사용 중인 ID 입니다."),
    INVALID_PASSWORD_FORMAT(BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
    INVALID_NAME_FORMAT(BAD_REQUEST, "이름 형식이 올바르지 않습니다."),
    INVALID_EMAIL_FORMAT(BAD_REQUEST,"이메일 형식이 올바르지 않습니다."),
    INVALID_PHONE_NUMBER(BAD_REQUEST,"전화번호 형식이 올바르지 않습니다."),
    PHONE_NUMBER_NOT_VERIFIED(BAD_REQUEST, "인증 되지 않은 전화번호입니다."),
    INVALID_VERIFICATION_CODE(BAD_REQUEST, "인증 번호가 일치하지 않습니다."),
    VERIFICATION_FAILED(BAD_REQUEST,"인증 번호 검증에 실패했습니다."),
    NO_VERIFICATION_RECORD(BAD_REQUEST,"인증 기록이 존재하지 않습니다."),
    ALREADY_VERIFIED(BAD_REQUEST, "이미 인증되었습니다."),
    TWILIO_ERROR(BAD_REQUEST, "Twilio 서비스 오류가 발생했습니다."),
    ID_DUPLICATION_CHECK_NOT_DONE(BAD_REQUEST,"ID 중복 체크가 되지 않았습니다."),
    ID_NOT_FOUND(BAD_REQUEST, "가입되지 않은 ID 입니다."),
    VERIFICATION_CODE_EXPIRED(BAD_REQUEST,"인증코드 만료"),
    USER_NOT_FOUND(BAD_REQUEST,"User를 찾을 수 없습니다."),
    CONSENT_REQUIRED(BAD_REQUEST,"(필수) 약관 동의가 되지 않았습니다."),
    EXPIRED_TOKEN(BAD_REQUEST, "만료된 토큰 입니다."),
    INVALID_PW(BAD_REQUEST,"비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST,"유효하지 않은 토큰 입니다."),
    RIDE_NOT_FOUND(BAD_REQUEST, "이용 기록을 찾을 수 없습니다"),
    BICYCLE_ALREADY_IN_USE(BAD_REQUEST, "사용 중인 자전거입니다."),
    BICYCLE_NOT_FOUND(BAD_REQUEST, "자전거를 찾을 수 없습니다."),
    INVALID_QR_CODE(BAD_REQUEST, "올바르지 않은 QR 코드가 입니다."),
    CARD_ALREADY_REGISTERED(BAD_REQUEST, "이미 등록된 카드입니다."),
    ACCOUNT_NOT_FOUND(BAD_REQUEST,"등록되어 있지 않은 계좌입니다."),
    ACCOUNT_ALREADY_EXISTS(BAD_REQUEST, "이미 등록된 계좌입니다."),
    INSUFFICIENT_POINTS(BAD_REQUEST, "포인트가 부족합니다."),
    UNAUTHORIZED_ACCESS(BAD_REQUEST,"사용자가 일치하지 않습니다."),
    RIDE_ALREADY_ENDED(BAD_REQUEST, "해당 라이딩은 이미 종료되었습니다.")

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
