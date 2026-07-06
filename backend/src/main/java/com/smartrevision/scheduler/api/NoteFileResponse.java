package com.smartrevision.scheduler.api;

import com.smartrevision.scheduler.note.NoteFile;

public record NoteFileResponse(
        Long id,
        String fileName,
        String contentType,
        long sizeBytes,
        String fileUrl
) {
    public static NoteFileResponse from(NoteFile noteFile) {
        return new NoteFileResponse(
                noteFile.getId(),
                noteFile.getOriginalFileName(),
                noteFile.getContentType(),
                noteFile.getSizeBytes(),
                noteFile.getFileUrl()
        );
    }
}
