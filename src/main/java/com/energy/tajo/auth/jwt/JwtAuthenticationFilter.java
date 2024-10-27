package com.energy.tajo.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
        throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String uuid = jwtTokenProvider.extractUuidFromAccessToken(token);

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(uuid, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 인증 로그 출력 - 추가
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                logger.debug("Successfully authenticated user: " + uuid);
                logger.debug("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            }
        }

        chain.doFilter(request, response);
    }
}
