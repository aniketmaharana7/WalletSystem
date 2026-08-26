package com.wallet.app.common.security;

import com.wallet.app.config.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        log.debug(
                "Authorization header present: {}",
                authorization != null
        );

        if (authorization == null ||
                !authorization.startsWith("Bearer ")) {

            log.debug("No Bearer token found");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);

        try {

            log.debug("Bearer token found. Validating token...");

            if (jwtService.validateToken(token)) {

                log.debug("JWT token is valid");

                UUID id =
                        UUID.fromString(
                                jwtService.extractUserId(token)
                        );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                id,
                                null,
                                List.of(
                                        new SimpleGrantedAuthority(
                                                "ROLE_USER"
                                        )
                                )
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(auth);

                log.debug(
                        "Authenticated user: {}",
                        id
                );

            } else {

                log.debug("JWT token is invalid");

            }

        } catch (Exception e) {

            log.error(
                    "JWT authentication failed",
                    e
            );

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}