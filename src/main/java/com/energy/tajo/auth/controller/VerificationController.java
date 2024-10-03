package com.energy.tajo.auth.controller;

import com.energy.tajo.auth.dto.request.VerifyCheckRequest;
import com.energy.tajo.auth.dto.request.VerifyCodeRequest;
import com.energy.tajo.auth.service.TwilioService;
import com.energy.tajo.global.success.SuccessCode;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@RequestMapping("/api/auth/verify")
public class VerificationController {

    private final TwilioService twilioService;
    private static final String MESSAGE_KEY = "message";

    public VerificationController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }


    // 인증 코드 전송
    @PostMapping("/sendcode")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@Valid @RequestBody VerifyCodeRequest request, HttpSession session) {
        try {
            twilioService.sendVerificationCode(request.phoneNum(), request.uuid(), session);
            return ResponseEntity.ok(Map.of(MESSAGE_KEY, SuccessCode.CODE_SENT.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(MESSAGE_KEY, e.getMessage()));
        }
    }


    // 코드 일치 여부 판단
    @PostMapping("/checkcode")
    public ResponseEntity<Object> verificationCheck(@RequestBody VerifyCheckRequest verifyCheckRequest, HttpSession session) {
        return twilioService.verificationCheck(verifyCheckRequest, session);
    }
}
