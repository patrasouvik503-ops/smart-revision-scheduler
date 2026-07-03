package com.smartrevision.scheduler.api;

import java.util.List;

public record StatisticsResponse(
        List<DayCountResponse> weeklyActivity,
        List<DayCountResponse> revisionConsistency,
        List<SubjectCountResponse> subjects
) {
}
