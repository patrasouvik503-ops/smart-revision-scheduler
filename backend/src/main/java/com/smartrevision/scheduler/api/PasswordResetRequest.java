package com.smartrevision.scheduler.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequest(
        @Email @NotBlank String email,
        @NotBlank String otp,
        @Size(min = 8, message = "Password must be at least 8 characters") String newPassword
) {
}
