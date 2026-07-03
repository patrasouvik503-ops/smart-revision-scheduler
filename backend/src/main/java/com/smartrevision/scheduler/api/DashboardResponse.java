package com.smartrevision.scheduler.api;

import java.util.List;

public record DashboardResponse(
        long topicsLearned,
        long revisionsCompleted,
        long currentStreak,
        int memoryScore,
        List<RevisionResponse> today,
        List<RevisionResponse> tomorrow,
        List<RevisionResponse> nextWeek
) {
}
