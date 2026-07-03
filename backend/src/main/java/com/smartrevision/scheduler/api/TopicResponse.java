package com.smartrevision.scheduler.api;

import com.smartrevision.scheduler.topic.Topic;
import java.time.LocalDate;
import java.util.List;

public record TopicResponse(
        Long id,
        String topicName,
        String subject,
        String difficulty,
        LocalDate dateLearned,
        String notes,
        List<NoteFileResponse> noteFiles,
        List<RevisionResponse> revisions
) {
    public static TopicResponse from(Topic topic) {
        return new TopicResponse(
                topic.getId(),
                topic.getTopicName(),
                topic.getSubject(),
                topic.getDifficulty().name(),
                topic.getDateLearned(),
                topic.getNotes(),
                topic.getNoteFiles().stream().map(NoteFileResponse::from).toList(),
                topic.getRevisions().stream().map(RevisionResponse::from).toList()
        );
    }
}
