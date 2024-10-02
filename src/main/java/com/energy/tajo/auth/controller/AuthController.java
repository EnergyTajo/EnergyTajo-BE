package com.energy.tajo.auth.controller;

import static com.energy.tajo.global.exception.ErrorCode.ID_IN_USE;

import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.success.SuccessCode;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.dto.request.UserCreateRequest;
import com.energy.tajo.user.dto.response.UserCreateResponse;
import com.energy.tajo.user.service.UserService;
import jakarta.validation.Valid;
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

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreateResponse signUp(@RequestBody @Valid final UserCreateRequest userCreateRequest) {
        User user = userService.signup(userCreateRequest);
        return UserCreateResponse.from(user);
    }

    // ID 중복 체크 API
    @GetMapping("/check-id")
    public ResponseEntity<String> checkUuid(@RequestParam("uuid") final String uuid) {
        boolean checkIdDuplication = userService.checkIdDuplication(uuid);

        if (checkIdDuplication) {
            return ResponseEntity.ok(SuccessCode.AVAILABLE_ID.getMessage());
        }
        else {
            throw new EnergyException(ID_IN_USE);
        }
    }
}
