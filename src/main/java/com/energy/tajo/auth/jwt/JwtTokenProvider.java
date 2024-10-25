package com.energy.tajo.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access.secret}")
    private String jwtAccessTokenSecret;

    @Value("${jwt.access.expiration}")
    private long jwtAccessTokenExpirationInMs;

    @Value("${jwt.refresh.secret}")
    private String jwtRefreshTokenSecret;

    @Value("${jwt.refresh.expiration}")
    private long jwtRefreshTokenExpirationInMs;

    private final Key secretKey;

    public JwtTokenProvider(@Value("${jwt.access.secret}") String accessSecret) {
        this.secretKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Access Token 생성
    public String generateAccessToken(String uuid) {
        return generateToken(uuid, jwtAccessTokenSecret, jwtAccessTokenExpirationInMs);
    }

    // Refresh Token 생성
    public String generateRefreshToken(String uuid) {
        return generateToken(uuid, jwtRefreshTokenSecret, jwtRefreshTokenExpirationInMs);
    }

    public String generateToken(String uuid, String secretKeyString, long expirationInMs) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + expirationInMs);
        final SecretKey generatedSecretKey = new SecretKeySpec(secretKeyString.getBytes(StandardCharsets.UTF_8),
            SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
            .setSubject(uuid)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(generatedSecretKey)
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

    // 요청 헤더에서 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(jwtAccessTokenSecret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 특정 클레임 추출 (For more flexibility)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 모든 클레임 추출
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }


    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}