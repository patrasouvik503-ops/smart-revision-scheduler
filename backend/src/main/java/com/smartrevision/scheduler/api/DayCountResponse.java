package com.smartrevision.scheduler.api;

import java.time.LocalDate;

public record DayCountResponse(
        LocalDate date,
        String label,
        long count
) {
}
