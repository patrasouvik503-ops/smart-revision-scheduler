package com.smartrevision.scheduler.api;

import com.smartrevision.scheduler.revision.Revision;
import java.time.LocalDate;
import java.util.List;

public record RevisionResponse(
        Long id,
        Long topicId,
        String topicName,
        String subject,
        String notes,
        List<NoteFileResponse> noteFiles,
        String difficulty,
        int revisionNumber,
        int revisionDay,
        LocalDate revisionDate,
        boolean completed
) {
    public static RevisionResponse from(Revision revision) {
        var topic = revision.getTopic();
        return new RevisionResponse(
                revision.getId(),
                topic.getId(),
                topic.getTopicName(),
                topic.getSubject(),
                topic.getNotes(),
                topic.getNoteFiles().stream().map(NoteFileResponse::from).toList(),
                topic.getDifficulty().name(),
                revision.getRevisionNumber(),
                revision.getRevisionDay(),
                revision.getRevisionDate(),
                revision.isCompleted()
        );
    }
}
