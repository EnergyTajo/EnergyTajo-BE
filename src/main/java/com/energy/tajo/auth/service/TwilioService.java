package com.energy.tajo.auth.service;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwilioService {
    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.verify_service_sid}")
    private String verifyServiceSid;

    public void sendVerificationCode(String to) {
        Twilio.init(accountSid, authToken);
        Verification verification = Verification.creator(
                to,
                verifyServiceSid,
                "sms")
            .create();
        log.info("Verification SID: {}", verification.getSid());
    }

}