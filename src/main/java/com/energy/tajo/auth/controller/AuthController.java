package com.energy.tajo.auth.controller;

import com.energy.tajo.auth.dto.request.RefreshTokenRequest;
import com.energy.tajo.auth.dto.request.SignInRequest;
import com.energy.tajo.auth.dto.response.TokenResponse;
import com.energy.tajo.auth.jwt.JwtTokenProvider;
import com.energy.tajo.auth.service.AuthenticationService;
import com.energy.tajo.global.encode.PasswordEncoderSHA256;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.dto.request.UserCreateRequest;
import com.energy.tajo.user.repository.UserRepository;
import com.energy.tajo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;

    // ID 중복 체크
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

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(@RequestBody UserCreateRequest request) {

        boolean phoneExists = userRepository.existsByPhoneNum(request.phoneNum());
        if (phoneExists) {
            return ResponseEntity.badRequest().body("이미 사용 중인 전화번호입니다.");
        }
        if (!userService.isIdChecked()) {
            throw new EnergyException(ErrorCode.ID_DUPLICATION_CHECK_NOT_DONE);
        }
        if (!userService.isSmsVerified()) {
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
        String token = jwtTokenProvider.generateAccessToken(user.getUuid());
        return ResponseEntity.ok(token);
    }


    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> login(@RequestBody SignInRequest signInRequest) {
        TokenResponse tokenResponse = authenticationService.login(signInRequest.uuid(), signInRequest.pw());
        return ResponseEntity.ok(tokenResponse);
    }

    // 로그아웃
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        String uuid = jwtTokenProvider.extractUuidFromAccessToken(token);
        authenticationService.invalidateRefreshToken(uuid);
    }


    // 토큰 갱신
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenResponse tokenResponse = authenticationService.refreshAccessToken(refreshTokenRequest.refreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

}
