package com.energy.tajo.auth.service;

import com.energy.tajo.user.service.UserService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.verify_service_sid}")
    private String verifyServiceSid;

    private final UserService userService;

    public TwilioService(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    // 인증 코드 전송
    public boolean sendVerificationCode(String phoneNumber) {
        try {
            Verification verification = Verification.creator(
                verifyServiceSid,
                phoneNumber,
                "sms"
            ).create();
            return verification.getStatus() != null;
        } catch (Exception e) {
            return false;
        }
    }

    // 인증 코드 확인
    public boolean verifyPhoneNumber(String phoneNumber, String verificationCode) {
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(
                verifyServiceSid,
                verificationCode
            ).setTo(phoneNumber).create();

            boolean isVerified = "approved".equals(verificationCheck.getStatus());
            if (isVerified) {
                userService.setSmsVerified(true);
                userService.setVerifiedPhoneNumber(phoneNumber);
            }
            return isVerified;
        } catch (Exception e) {
            return false;
        }
    }
}
