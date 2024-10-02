package com.energy.tajo.auth.service;

import com.energy.tajo.auth.domain.TwilioVerify;
import com.energy.tajo.auth.dto.request.VerifyCheckRequest;
import com.energy.tajo.auth.repository.TwilioVerifyRepository;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.global.success.SuccessCode;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final TwilioVerifyRepository twilioVerifyRepository;

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.verify_service_sid}")
    private String verifyServiceSid;

    public TwilioService(TwilioVerifyRepository twilioVerifyRepository) {
        this.twilioVerifyRepository = twilioVerifyRepository;
    }

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    // 인증 번호 요청
    public boolean sendVerificationCode(String to) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(to, "KR");
            String formattedNumber = phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

            Verification.creator(
                    formattedNumber,
                    verifyServiceSid,
                    "sms")
                .create();
            return true;
        } catch (NumberParseException e) {
            throw new EnergyException(ErrorCode.INVALID_INPUT_VALUE);
        } catch (Exception e) {
            throw new EnergyException(ErrorCode.SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> verificationCheck(VerifyCheckRequest verifyCheckRequest) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        try {
            var numberProto = phoneUtil.parse(verifyCheckRequest.phoneNum(), "KR");
            var e164FormatPhoneNumber = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
            Optional<TwilioVerify> optionalVerifyRecord = twilioVerifyRepository.findByPhoneNum(e164FormatPhoneNumber);

            if (optionalVerifyRecord.isEmpty()) {
                throw new EnergyException(ErrorCode.NO_VERIFICATION_RECORD);
            }

            TwilioVerify verificationRecord = optionalVerifyRecord.get();

            if (verificationRecord.isVerified()) {
                throw new EnergyException(ErrorCode.ALREADY_VERIFIED);
            }

            VerificationCheck verificationCheck = VerificationCheck.creator(verifyServiceSid, verifyCheckRequest.verificationCode())
                .setTo(e164FormatPhoneNumber)
                .create();

            // 인증 성공 시
            if ("approved".equals(verificationCheck.getStatus())) {
                verificationRecord.markAsVerified();
                twilioVerifyRepository.save(verificationRecord);
                return ResponseEntity.ok(SuccessCode.VERIFICATION_SUCCESSFUL.getMessage());

            } else {
                throw new EnergyException(ErrorCode.VERIFICATION_FAILED);
            }

        } catch (NumberParseException e) {
            throw new EnergyException(ErrorCode.INVALID_PHONE_NUMBER);
        } catch (TwilioException e) {
            throw new EnergyException(ErrorCode.TWILIO_ERROR);
        } catch (Exception e) {
            throw new EnergyException(ErrorCode.SERVER_ERROR);
        }
    }
}