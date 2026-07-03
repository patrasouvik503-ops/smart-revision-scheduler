package com.smartrevision.scheduler.revision;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevisionRepository extends JpaRepository<Revision, Long> {

    List<Revision> findByRevisionDateOrderByCompletedAscRevisionNumberAsc(LocalDate revisionDate);

    List<Revision> findByRevisionDateBetweenOrderByRevisionDateAscRevisionNumberAsc(LocalDate from, LocalDate to);

    List<Revision> findByTopicUserIdAndRevisionDateOrderByCompletedAscRevisionNumberAsc(Long userId, LocalDate revisionDate);

    List<Revision> findByTopicUserIdAndRevisionDateBetweenOrderByRevisionDateAscRevisionNumberAsc(Long userId, LocalDate from, LocalDate to);

    List<Revision> findByTopicUserIdAndRevisionDateLessThanEqualOrderByRevisionDateAscRevisionNumberAsc(Long userId, LocalDate revisionDate);

    List<Revision> findByTopicUserIdOrderByRevisionDateAscRevisionNumberAsc(Long userId);

    List<Revision> findByTopicUserIdAndCompletedTrueAndCompletedAtBetweenOrderByCompletedAtAsc(Long userId, Instant from, Instant to);

    long countByTopicUserIdAndCompletedTrue(Long userId);

    long countByTopicUserIdAndCompletedTrueAndCompletedAtAfter(Long userId, Instant after);
}
