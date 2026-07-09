package com.smartrevision.scheduler.service;

import com.smartrevision.scheduler.api.DayCountResponse;
import com.smartrevision.scheduler.api.DashboardResponse;
import com.smartrevision.scheduler.api.RevisionResponse;
import com.smartrevision.scheduler.api.StatisticsResponse;
import com.smartrevision.scheduler.api.SubjectCountResponse;
import com.smartrevision.scheduler.revision.Revision;
import com.smartrevision.scheduler.revision.RevisionRepository;
import com.smartrevision.scheduler.topic.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RevisionService {

    private final RevisionRepository revisionRepository;
    private final TopicRepository topicRepository;

    public RevisionService(RevisionRepository revisionRepository, TopicRepository topicRepository) {
        this.revisionRepository = revisionRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public List<RevisionResponse> today(Long userId) {
        return dueRevisionsUntil(userId, LocalDate.now());
    }

    @Transactional
    public List<RevisionResponse> calendar(Long userId, LocalDate from, LocalDate to) {
        return revisionRepository.findByTopicUserIdAndRevisionDateBetweenOrderByRevisionDateAscRevisionNumberAsc(userId, from, to)
                .stream()
                .map(RevisionResponse::from)
                .toList();
    }

    @Transactional
    public DashboardResponse dashboard(Long userId) {
        LocalDate today = LocalDate.now();
        List<RevisionResponse> todayRevisions = dueRevisionsUntil(userId, today);
        List<RevisionResponse> tomorrow = revisionsOn(userId, today.plusDays(1));
        List<RevisionResponse> nextWeek = calendar(userId, today.plusDays(2), today.plusDays(7));
        long topicsLearned = topicRepository.countByUserId(userId);
        long completed = revisionRepository.countByTopicUserIdAndCompletedTrue(userId);
        long currentStreak = calculateCurrentStreak(userId);
        return new DashboardResponse(
                topicsLearned,
                completed,
                currentStreak,
                calculateMemoryScore(userId, today, currentStreak),
                todayRevisions,
                tomorrow,
                nextWeek
        );
    }

    @Transactional
    public StatisticsResponse statistics(Long userId) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate heatmapStart = today.minusDays(90);

        List<Revision> completedThisWeek = completedBetween(userId, weekStart, weekStart.plusDays(7), zone);
        List<Revision> completedForHeatmap = completedBetween(userId, heatmapStart, today.plusDays(1), zone);

        Map<LocalDate, Long> weeklyCounts = countCompletedByDate(completedThisWeek, zone);
        Map<LocalDate, Long> heatmapCounts = countCompletedByDate(completedForHeatmap, zone);

        List<DayCountResponse> weeklyActivity = weekStart.datesUntil(weekStart.plusDays(7))
                .map(date -> new DayCountResponse(
                        date,
                        date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        weeklyCounts.getOrDefault(date, 0L)
                ))
                .toList();

        List<DayCountResponse> revisionConsistency = heatmapStart.datesUntil(today.plusDays(1))
                .map(date -> new DayCountResponse(
                        date,
                        date.toString(),
                        heatmapCounts.getOrDefault(date, 0L)
                ))
                .toList();

        List<SubjectCountResponse> subjects = revisionRepository.findByTopicUserIdOrderByRevisionDateAscRevisionNumberAsc(userId)
                .stream()
                .collect(Collectors.groupingBy(revision -> revision.getTopic().getSubject(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .map(entry -> new SubjectCountResponse(entry.getKey(), entry.getValue()))
                .toList();

        return new StatisticsResponse(weeklyActivity, revisionConsistency, subjects);
    }

    @Transactional
    public RevisionResponse complete(Long userId, Long revisionId) {
        Revision revision = revisionRepository.findById(revisionId)
                .orElseThrow(() -> new EntityNotFoundException("Revision not found"));
        if (!userId.equals(revision.getTopic().getUserId())) {
            throw new EntityNotFoundException("Revision not found");
        }
        revision.markCompleted();
        return RevisionResponse.from(revision);
    }

    private List<RevisionResponse> revisionsOn(Long userId, LocalDate date) {
        return revisionRepository.findByTopicUserIdAndRevisionDateOrderByCompletedAscRevisionNumberAsc(userId, date)
                .stream()
                .map(RevisionResponse::from)
                .toList();
    }

    private List<RevisionResponse> dueRevisionsUntil(Long userId, LocalDate date) {
        return revisionRepository.findByTopicUserIdAndRevisionDateLessThanEqualOrderByRevisionDateAscRevisionNumberAsc(userId, date)
                .stream()
                .map(RevisionResponse::from)
                .toList();
    }

    private List<Revision> completedBetween(Long userId, LocalDate from, LocalDate to, ZoneId zone) {
        return revisionRepository.findByTopicUserIdAndCompletedTrueAndCompletedAtBetweenOrderByCompletedAtAsc(
                userId,
                from.atStartOfDay(zone).toInstant(),
                to.atStartOfDay(zone).toInstant()
        );
    }

    private Map<LocalDate, Long> countCompletedByDate(List<Revision> revisions, ZoneId zone) {
        return revisions.stream()
                .filter(revision -> revision.getCompletedAt() != null)
                .map(revision -> revision.getCompletedAt().atZone(zone).toLocalDate())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private long calculateCurrentStreak(Long userId) {
        ZoneId zone = ZoneId.systemDefault();
        long streak = 0;
        LocalDate cursor = LocalDate.now();

        while (true) {
            Instant startOfDay = cursor.atStartOfDay(zone).toInstant();
            Instant endOfDay = cursor.plusDays(1).atStartOfDay(zone).toInstant();
            long dayCount = revisionRepository.countByTopicUserIdAndCompletedTrueAndCompletedAtAfter(userId, startOfDay)
                    - revisionRepository.countByTopicUserIdAndCompletedTrueAndCompletedAtAfter(userId, endOfDay);
            if (dayCount == 0) {
                return streak;
            }
            streak++;
            cursor = cursor.minusDays(1);
        }
    }

    private int calculateMemoryScore(Long userId, LocalDate today, long currentStreak) {
        List<Revision> dueRevisions = revisionRepository
                .findByTopicUserIdAndRevisionDateLessThanEqualOrderByRevisionDateAscRevisionNumberAsc(userId, today);
        if (dueRevisions.isEmpty()) {
            return 0;
        }

        long completedDue = dueRevisions.stream().filter(Revision::isCompleted).count();
        long completedOnTime = dueRevisions.stream()
                .filter(Revision::isCompleted)
                .filter(this::wasCompletedOnTime)
                .count();

        double totalDifficultyWeight = dueRevisions.stream()
                .mapToDouble(this::difficultyWeight)
                .sum();
        double completedDifficultyWeight = dueRevisions.stream()
                .filter(Revision::isCompleted)
                .mapToDouble(this::difficultyWeight)
                .sum();

        double completionScore = (completedDue * 100.0 / dueRevisions.size()) * 0.40;
        double onTimeScore = completedDue == 0 ? 0 : (completedOnTime * 100.0 / completedDue) * 0.30;
        double streakScore = Math.min(currentStreak, 7) * 100.0 / 7 * 0.20;
        double difficultyScore = totalDifficultyWeight == 0 ? 0 : (completedDifficultyWeight * 100.0 / totalDifficultyWeight) * 0.10;

        return (int) Math.min(100, Math.round(completionScore + onTimeScore + streakScore + difficultyScore));
    }

    private boolean wasCompletedOnTime(Revision revision) {
        if (revision.getCompletedAt() == null) {
            return false;
        }
        LocalDate completedDate = revision.getCompletedAt()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return !completedDate.isAfter(revision.getRevisionDate());
    }

    private double difficultyWeight(Revision revision) {
        return switch (revision.getTopic().getDifficulty()) {
            case EASY -> 1.0;
            case MEDIUM -> 1.15;
            case HARD -> 1.3;
        };
    }
}
