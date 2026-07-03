package com.smartrevision.scheduler.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String CURRENT_USER_ATTRIBUTE = "currentUser";

    private final AccessTokenRepository accessTokenRepository;
    private final TokenHasher tokenHasher;

    public AuthInterceptor(AccessTokenRepository accessTokenRepository, TokenHasher tokenHasher) {
        this.accessTokenRepository = accessTokenRepository;
        this.tokenHasher = tokenHasher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || request.getRequestURI().startsWith("/api/auth")) {
            return true;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        String token = header.substring("Bearer ".length()).trim();
        return accessTokenRepository.findByTokenHashAndExpiresAtAfter(tokenHasher.sha256(token), Instant.now())
                .map(accessToken -> {
                    request.setAttribute(CURRENT_USER_ATTRIBUTE, new CurrentUser(
                            accessToken.getUser().getId(),
                            accessToken.getUser().getEmail()
                    ));
                    return true;
                })
                .orElseGet(() -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                });
    }
}
