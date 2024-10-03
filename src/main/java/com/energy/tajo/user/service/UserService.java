package com.energy.tajo.user.service;

import static com.energy.tajo.global.exception.ErrorCode.INVALID_EMAIL_FORMAT;
import static com.energy.tajo.global.exception.ErrorCode.INVALID_NAME_FORMAT;
import static com.energy.tajo.global.exception.ErrorCode.INVALID_PASSWORD_FORMAT;

import com.energy.tajo.auth.service.TwilioService;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.dto.request.UserCreateRequest;
import com.energy.tajo.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TwilioService twilioService;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NAME_REGEX = "^[가-힣]{2,10}$";
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}$";

    // 비밀번호 형식 검증
    private void validatePassword(String password) {
        if (!password.matches(PASSWORD_REGEX)) {
            throw new EnergyException(INVALID_PASSWORD_FORMAT);
        }
    }


    // 회원가입
    @Transactional
    public User signup(UserCreateRequest userCreateRequest, Boolean isIdChecked, Boolean isSmsVerified) {

        if (isIdChecked == null || !isIdChecked) {
            throw new EnergyException(ErrorCode.ID_DUPLICATION_CHECK_NOT_DONE);
        }

        if (isSmsVerified == null || !isSmsVerified) {
            throw new EnergyException(ErrorCode.PHONE_NUMBER_NOT_VERIFIED);
        }

        validatePassword(userCreateRequest.pw());

        if (!Pattern.matches(NAME_REGEX, userCreateRequest.name())) {
            throw new EnergyException(INVALID_NAME_FORMAT);
        }
        if (!Pattern.matches(EMAIL_REGEX, userCreateRequest.email())) {
            throw new EnergyException(INVALID_EMAIL_FORMAT);
        }


        User user = User.of(userCreateRequest.uuid(), userCreateRequest.pw(), userCreateRequest.name(),
            userCreateRequest.email(), userCreateRequest.consentStatus());
        user.setPoints(0);

        return userRepository.save(user);
    }

    // UserService.java
    @Transactional
    public void checkIdAndSendSms(String uuid, String phoneNum, HttpSession session) {
        if (!isIdChecked(uuid)) {
            throw new EnergyException(ErrorCode.ID_DUPLICATION_CHECK_NOT_DONE);
        }
        // SMS 인증 요청
        twilioService.sendVerificationCode(phoneNum, uuid, session);
    }


    // ID 체크 여부 확인 (추가 보안용)
    @Transactional(readOnly = true)
    public boolean isIdChecked(String uuid) {
        boolean isChecked = userRepository.existsByUuid(uuid);
        System.out.println("ID Checked: " + isChecked);
        return isChecked;
    }
}
