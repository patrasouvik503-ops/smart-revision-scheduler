package com.smartrevision.scheduler.api;

public record AuthResponse(
        String token,
        Long userId,
        String email
) {
}
