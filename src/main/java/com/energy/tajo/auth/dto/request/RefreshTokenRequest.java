package com.energy.tajo.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
    @NotNull(message = "리프레시 토큰 없음")
    String refreshToken
) { }
