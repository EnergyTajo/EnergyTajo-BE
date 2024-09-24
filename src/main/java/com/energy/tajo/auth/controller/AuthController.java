package com.energy.tajo.auth.controller;

import com.energy.tajo.user.dto.request.UserCreateRequest;
import com.energy.tajo.user.dto.response.UserCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreateResponse signUp(@RequestBody @Valid final UserCreateRequest userCreateRequest) {
    }
}
