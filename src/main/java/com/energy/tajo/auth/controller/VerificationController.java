package com.energy.tajo.auth.controller;

import com.energy.tajo.auth.dto.request.VerifyCheckRequest;
import com.energy.tajo.auth.service.TwilioService;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.user.service.UserService;
import com.energy.tajo.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth/verify")
@RequiredArgsConstructor
public class VerificationController {

    private final TwilioService twilioService;
    private final UserService userService;

    // SMS 인증 코드 전송
    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestBody VerifyCheckRequest verifyCheckRequest) {
        String e164FormatPhoneNumber = CommonUtils.getE164FormatPhoneNumber(verifyCheckRequest.phoneNum());
        boolean isSent = twilioService.sendVerificationCode(e164FormatPhoneNumber);
        if (isSent) {
            return ResponseEntity.ok("인증 번호 요청 성공");
        } else {
            throw new EnergyException(ErrorCode.INVALID_REQUEST);
        }
    }

    // 인증 코드 검증
    @PostMapping("/check-code")
    public ResponseEntity<String> verificationCheck(@RequestBody VerifyCheckRequest verifyCheckRequest) {
        String e164FormatPhoneNumber = CommonUtils.getE164FormatPhoneNumber(verifyCheckRequest.phoneNum());
        try {
            boolean isVerified = twilioService.verifyPhoneNumber(e164FormatPhoneNumber, verifyCheckRequest.verificationCode());
            if (isVerified) {
                userService.setSmsVerified(true);
                userService.setVerifiedPhoneNumber(e164FormatPhoneNumber);
                return ResponseEntity.ok("인증 번호 검증 성공");
            } else {
                throw new EnergyException(ErrorCode.VERIFICATION_FAILED);
            }
        } catch (Exception e) {
            throw new EnergyException(ErrorCode.VERIFICATION_FAILED);
        }
    }
}