package com.smartrevision.scheduler.topic;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    long countByUserId(Long userId);

    Optional<Topic> findByIdAndUserId(Long id, Long userId);
}
