package com.smartrevision.scheduler.api;

import com.smartrevision.scheduler.topic.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AddTopicRequest(
        @NotBlank String topicName,
        @NotBlank String subject,
        @NotNull Difficulty difficulty,
        @NotNull LocalDate dateLearned,
        String notes
) {
}
