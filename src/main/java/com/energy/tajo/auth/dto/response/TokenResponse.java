package com.energy.tajo.auth.dto.response;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public record TokenResponse(String accessToken, String refreshToken) {
    public static void expireAccessToken(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from(HttpHeaders.AUTHORIZATION, "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }
}