package com.energy.tajo.user.service;

import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class UserService {

    private boolean idChecked = false;
    private boolean smsVerified = false;
    private String checkedUuid;
    private String verifiedPhoneNumber;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NAME_REGEX = "^[가-힣]{2,10}$";
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";

    // 이메일 형식 검증
    public void validateEmail(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new EnergyException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    // 이름 형식 검증
    public void validateName(String name) {
        if (!name.matches(NAME_REGEX)) {
            throw new EnergyException(ErrorCode.INVALID_NAME_FORMAT);
        }
    }

    // 비밀번호 형식 검증
    public void validatePassword(String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new EnergyException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }
}