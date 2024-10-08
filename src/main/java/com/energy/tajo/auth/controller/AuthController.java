package com.energy.tajo.auth.controller;

import com.energy.tajo.global.encode.PasswordEncoderSHA256;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.security.JwtUtil;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.dto.request.UserCreateRequest;
import com.energy.tajo.user.repository.UserRepository;
import com.energy.tajo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // ID 중복 체크 API
    @GetMapping("/check-id")
    public ResponseEntity<String> checkIdDuplicate(@RequestParam String uuid) {
        if (userRepository.existsByUuid(uuid)) {
            throw new EnergyException(ErrorCode.ID_IN_USE);
        } else {
            userService.setIdChecked(true);
            userService.setCheckedUuid(uuid);
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        }
    }

    // 회원가입 api
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@RequestBody UserCreateRequest request) {

        boolean phoneExists = userRepository.existsByPhoneNum(request.phoneNum());
        if (phoneExists) {
            return ResponseEntity.badRequest().body("이미 사용 중인 전화번호입니다.");
        }
        if (!userService.isIdChecked()) {
            throw new EnergyException(ErrorCode.ID_DUPLICATION_CHECK_NOT_DONE);
        }
        if(!userService.isSmsVerified()){
            throw new EnergyException(ErrorCode.PHONE_NUMBER_NOT_VERIFIED);
        }
        if (!request.consentStatus()) {
            throw new EnergyException(ErrorCode.CONSENT_REQUIRED);
        }

        userService.validateName(request.name());
        userService.validateEmail(request.email());
        userService.validatePassword(request.pw());

        String encryptedPassword = PasswordEncoderSHA256.encode(request.pw());
        String checkedUuid = userService.getCheckedUuid();
        String verifiedPhoneNumber = userService.getVerifiedPhoneNumber();
        User user = User.of(checkedUuid, encryptedPassword, request.name(), request.email(), request.consentStatus());
        user.setPhoneNum(verifiedPhoneNumber);
        userRepository.save(user);

        // JWT 생성
        String token = jwtUtil.generateToken(user.getUuid(), "user_flag");
        return ResponseEntity.ok(token);
    }
}
