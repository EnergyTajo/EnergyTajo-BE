package com.energy.tajo.auth.service;

import com.energy.tajo.auth.domain.TwilioVerify;
import com.energy.tajo.auth.dto.request.VerifyCheckRequest;
import com.energy.tajo.auth.repository.TwilioVerifyRepository;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.repository.UserRepository;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TwilioService {

    private final TwilioVerifyRepository twilioVerifyRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(TwilioService.class); //

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.verify_service_sid}")
    private String verifyServiceSid;


    public TwilioService(TwilioVerifyRepository twilioVerifyRepository, UserRepository userRepository) {
        this.twilioVerifyRepository = twilioVerifyRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    // 인증 번호 요청
    @Transactional
    public boolean sendVerificationCode(String to, String uuid, HttpSession session) {
        // 세션에 저장된 값을 로그로 출력
        logger.info("Session UUID: {}", session.getAttribute("uuid"));
        logger.info("Is ID Checked: {}", session.getAttribute("isIdChecked"));


        if (uuid == null) {
            throw new EnergyException(ErrorCode.ID_NOT_FOUND);
        }

        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(to, "KR");
            String formattedNumber = phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

            Verification verification = Verification.creator(verifyServiceSid, formattedNumber, "sms").create();

            // 확인 용
            logger.info("전화번호: {}", formattedNumber);
            logger.info("인증 코드: {}", verification.getSid());

            TwilioVerify twilioVerify = new TwilioVerify();
            twilioVerify.setPhoneNum(formattedNumber);
            twilioVerify.setVerificationCode(verification.getSid());
            twilioVerify.setVerified(false);
            twilioVerify.setCreatedAt(LocalDateTime.now());

            //
            Optional<User> optionalUser = userRepository.findByUuid(uuid);
            if (optionalUser.isPresent()) {
                twilioVerify.setUser(optionalUser.get());
            } else {
                throw new EnergyException(ErrorCode.USER_NOT_FOUND);
            }
            twilioVerifyRepository.save(twilioVerify);
            //

            return true;
        } catch (NumberParseException e) {
            logger.error("전화번호 파싱 오류: {}", e.getMessage());  //
            throw new EnergyException(ErrorCode.INVALID_INPUT_VALUE);
        } catch (TwilioException e) {
            logger.error("Twilio API 오류: {}", e.getMessage());  //
            throw new EnergyException(ErrorCode.TWILIO_ERROR);
        } catch (Exception e) {
            logger.error("서버 오류: {}", e.getMessage());     //
            throw new EnergyException(ErrorCode.SERVER_ERROR);
        }
    }



    // 코드 일치 여부 판단
    @Transactional
    public ResponseEntity<Object> verificationCheck(VerifyCheckRequest verifyCheckRequest, HttpSession session) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        String e164FormatPhoneNumber;

        String uuid = (String) session.getAttribute("uuid");
        try {
            var numberProto = phoneUtil.parse(verifyCheckRequest.phoneNum(), "KR");
            e164FormatPhoneNumber = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            logger.error("전화번호 파싱 오류: {}", e.getMessage());   //
            return new ResponseEntity<>("잘못된 전화번호 형식", HttpStatus.BAD_REQUEST);
        }

        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(verifyServiceSid, verifyCheckRequest.verificationCode())
                .setTo(e164FormatPhoneNumber)
                .create();

            logger.info("인증 상태: {}", verificationCheck.getStatus());    //

            if ("approved".equals(verificationCheck.getStatus())) {
                TwilioVerify twilioVerify = new TwilioVerify();
                twilioVerify.setPhoneNum(e164FormatPhoneNumber);
                twilioVerify.setVerificationCode(verifyCheckRequest.verificationCode());
                twilioVerify.setVerified(true);
                twilioVerify.setCreatedAt(LocalDateTime.now());

                if (uuid != null) {
                    Optional<User> optionalUser = userRepository.findByUuid(uuid);
                    if (optionalUser.isPresent()) {
                        twilioVerify.setUser(optionalUser.get());
                    } else {
                        throw new EnergyException(ErrorCode.USER_NOT_FOUND);
                    }
                } else {
                    throw new EnergyException(ErrorCode.ID_NOT_FOUND);
                }


                twilioVerifyRepository.save(twilioVerify);
                logger.info("인증 정보가 데이터베이스에 저장되었습니다.");   //
                return new ResponseEntity<>("인증 번호 검증 성공", HttpStatus.OK);
            } else {
                logger.error("인증 실패: {}", verificationCheck.getStatus());    //
                return new ResponseEntity<>("인증 번호 검증 실패", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("인증 번호 검증 실패: {}", e.getMessage());    //
            return new ResponseEntity<>("인증 번호 검증 실패", HttpStatus.BAD_REQUEST);
        }
    }
}
