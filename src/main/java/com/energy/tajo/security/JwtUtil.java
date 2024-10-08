package com.energy.tajo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key secretKey;

    // JWT 비밀키 주입
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 특정 클레임 추출
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

    // 토큰 생성
    public String generateToken(String uuid, String flag) {
        return Jwts.builder()
            .setSubject(uuid)
            .claim("flag", flag)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }


    public String updateToken(String token, String newFlag) {
        Claims claims = extractAllClaims(token);
        claims.put("flag", newFlag);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(claims.getSubject())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    // 토큰 만료 확인
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
