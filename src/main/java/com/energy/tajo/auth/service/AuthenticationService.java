package com.energy.tajo.auth.service;

import com.energy.tajo.auth.dto.response.TokenResponse;
import com.energy.tajo.auth.jwt.JwtTokenProvider;
import com.energy.tajo.global.encode.PasswordEncoderSHA256;
import com.energy.tajo.global.exception.EnergyException;
import com.energy.tajo.global.exception.ErrorCode;
import com.energy.tajo.user.domain.User;
import com.energy.tajo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(String uuid, String password) {

        User user = userRepository.findByUuid(uuid)
            .orElseThrow(() -> new EnergyException(ErrorCode.ID_NOT_FOUND));

        if (!PasswordEncoderSHA256.matches(password, user.getPw())) {
            throw new EnergyException(ErrorCode.INVALID_PW);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUuid());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUuid());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshAccessToken(String refreshToken) {

        String uuid = jwtTokenProvider.extractUuidFromRefreshToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(uuid);
        return new TokenResponse(newAccessToken, refreshToken);
    }

    public void invalidateRefreshToken(String uuid) {
        User user = userRepository.findByUuid(uuid)
            .orElseThrow(() -> new EnergyException(ErrorCode.USER_NOT_FOUND));

        user.setRefreshToken(null);
        userRepository.save(user);
    }
}
