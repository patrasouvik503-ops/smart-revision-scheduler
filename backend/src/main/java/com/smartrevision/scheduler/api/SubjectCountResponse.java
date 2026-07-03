package com.smartrevision.scheduler.api;

public record SubjectCountResponse(
        String subject,
        long count
) {
}
