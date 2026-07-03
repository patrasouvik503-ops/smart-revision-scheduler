package com.smartrevision.scheduler.auth;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByTokenHashAndExpiresAtAfter(String tokenHash, Instant now);
}
