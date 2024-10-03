package com.energy.tajo.global.success;

import lombok.Getter;

@Getter
public enum SuccessCode {

    CODE_SENT("인증 코드가 성공적으로 전송되었습니다."),
    VERIFICATION_SUCCESS("인증이 성공적으로 완료되었습니다."),
    AVAILABLE_ID("사용 가능한 아이디입니다."),
    VERIFICATION_SUCCESSFUL("인증 번호 검증 성공");

    private final String message;

    SuccessCode(String message) {
        this.message = message;
    }
}





