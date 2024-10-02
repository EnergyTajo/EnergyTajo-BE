package com.energy.tajo.user.service;

import static com.energy.tajo.global.exception.ErrorCode.ID_IN_USE;
import static com.energy.tajo.global.exception.ErrorCode.PHONE_NUMBER_NOT_VERIFIED;

import com.energy.tajo.auth.domain.TwilioVerify;
import com.energy.tajo.auth.repository.TwilioVerifyRepository;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.dto.request.UserCreateRequest;
import com.energy.tajo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TwilioVerifyRepository twilioVerifyRepository;

    // 회원가입
    @Transactional
    public User signup(UserCreateRequest userCreateRequest) {

        if (userRepository.existsByUuid(userCreateRequest.uuid())){
            throw new EnergyException(ID_IN_USE);
        }

        TwilioVerify verification = twilioVerifyRepository.findByPhoneNum(userCreateRequest.phone_num())
            .orElseThrow(() -> new EnergyException(PHONE_NUMBER_NOT_VERIFIED));

        if (!verification.isVerified()) {
            throw new EnergyException(PHONE_NUMBER_NOT_VERIFIED);
        }

        User user = User.of(userCreateRequest.uuid(), userCreateRequest.pw(), userCreateRequest.name(),
             userCreateRequest.email(), userCreateRequest.consent_status());
        user.setPoints(0);
        return userRepository.save(user);
    }

    // ID 중복 체크
    @Transactional(readOnly = true)
    public boolean checkIdDuplication(String uuid) {
        return !userRepository.existsByUuid(uuid);
    }
}
