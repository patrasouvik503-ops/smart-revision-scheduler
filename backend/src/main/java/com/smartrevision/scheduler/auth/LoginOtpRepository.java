package com.smartrevision.scheduler.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginOtpRepository extends JpaRepository<LoginOtp, Long> {

    Optional<LoginOtp> findTopByEmailAndPurposeAndUsedFalseOrderByCreatedAtDesc(String email, OtpPurpose purpose);
}
