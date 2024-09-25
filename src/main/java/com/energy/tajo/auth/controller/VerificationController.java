package com.energy.tajo.auth.controller;

import static com.energy.tajo.global.exception.ErrorCode.INVALID_VERIFICATION_CODE;

import com.energy.tajo.auth.service.TwilioService;
import com.energy.tajo.global.exception.EnergyException;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Random;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {

    private final TwilioService twilioService;
    private final Random random = new Random();

    public VerificationController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @PostMapping("/verify/phoneNum")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestParam String phoneNumber, HttpSession session) {

        String verificationCode = String.format("%06d", random.nextInt(999999));
        session.setAttribute("verificationCode", verificationCode);
        session.setMaxInactiveInterval(60);  // 세션 유효 시간 - 60초
        twilioService.sendVerificationCode(phoneNumber);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/verify/code")
    public ResponseEntity<Map<String, String>> verifyCode(@RequestParam String inputCode, HttpSession session) {
        String actualCode = (String) session.getAttribute("verificationCode");

        if (actualCode != null && actualCode.equals(inputCode)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new EnergyException(INVALID_VERIFICATION_CODE);
        }
    }
}
