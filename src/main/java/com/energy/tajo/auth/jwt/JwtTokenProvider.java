package com.energy.tajo.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.access.secret}")
    private String jwtAccessTokenSecret;

    @Value("${jwt.access.expiration}")
    private long jwtAccessTokenExpirationInMs;

    @Value("${jwt.refresh.secret}")
    private String jwtRefreshTokenSecret;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshTokenExpirationInMs;

    // Access Token 생성
    public String generateAccessToken(String uuid) {
        return generateToken(uuid, jwtAccessTokenSecret, jwtAccessTokenExpirationInMs);
    }

    // Refresh Token 생성
    public String generateRefreshToken(String uuid) {
        return generateToken(uuid, jwtRefreshTokenSecret, jwtRefreshTokenExpirationInMs);
    }

    private String generateToken(String uuid, String secretKeyString, long expirationInMs) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + expirationInMs);
        final SecretKey secretKey = new SecretKeySpec(secretKeyString.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
            .setSubject(uuid)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact();
    }

    // Token에서 UUID 추출
    public String extractUuidFromToken(String token, String secretKey) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    // Access Token에서 UUID 추출
    public String extractUuidFromAccessToken(String token) {
        return extractUuidFromToken(token, jwtAccessTokenSecret);
    }

    // Refresh Token에서 UUID 추출
    public String extractUuidFromRefreshToken(String token) {
        return extractUuidFromToken(token, jwtRefreshTokenSecret);
    }
}