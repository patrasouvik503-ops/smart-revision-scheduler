package com.smartrevision.scheduler.service;

import com.smartrevision.scheduler.api.AddTopicRequest;
import com.smartrevision.scheduler.api.NoteFileResponse;
import com.smartrevision.scheduler.api.TopicResponse;
import com.smartrevision.scheduler.note.NoteFile;
import com.smartrevision.scheduler.note.NoteFileRepository;
import com.smartrevision.scheduler.revision.Revision;
import com.smartrevision.scheduler.topic.Topic;
import com.smartrevision.scheduler.topic.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TopicService {

    private static final int[] REVISION_DAYS = {1, 3, 7, 14, 30, 60, 90};
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final TopicRepository topicRepository;
    private final NoteFileRepository noteFileRepository;
    private final Path uploadDirectory;

    public TopicService(
            TopicRepository topicRepository,
            NoteFileRepository noteFileRepository,
            @Value("${app.upload.dir:uploads}") String uploadDirectory
    ) {
        this.topicRepository = topicRepository;
        this.noteFileRepository = noteFileRepository;
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    @Transactional
    public TopicResponse addTopic(Long userId, AddTopicRequest request) {
        Topic topic = new Topic();
        topic.setUserId(userId);
        topic.setTopicName(request.topicName().trim());
        topic.setSubject(request.subject().trim());
        topic.setDifficulty(request.difficulty());
        topic.setDateLearned(request.dateLearned());
        topic.setNotes(cleanNotes(request.notes()));

        for (int i = 0; i < REVISION_DAYS.length; i++) {
            Revision revision = new Revision();
            revision.setRevisionNumber(i + 1);
            revision.setRevisionDay(REVISION_DAYS[i]);
            revision.setRevisionDate(request.dateLearned().plusDays(REVISION_DAYS[i]));
            topic.addRevision(revision);
        }

        return TopicResponse.from(topicRepository.save(topic));
    }

    @Transactional
    public List<NoteFileResponse> addNoteFiles(Long userId, Long topicId, List<MultipartFile> files) {
        Topic topic = topicRepository.findByIdAndUserId(topicId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create upload directory");
        }

        return files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(file -> saveNoteFile(topic, file))
                .map(NoteFileResponse::from)
                .toList();
    }

    public NoteFileDownload loadNoteFile(Long userId, Long fileId) {
        NoteFile noteFile = noteFileRepository.findByIdAndTopicUserId(fileId, userId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));
        Path filePath = uploadDirectory.resolve(noteFile.getStoredFileName()).normalize();
        if (!filePath.startsWith(uploadDirectory)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path");
        }
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
            return new NoteFileDownload(noteFile, resource);
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    @Transactional
    public void deleteTopic(Long userId, Long topicId) {
        Topic topic = topicRepository.findByIdAndUserId(topicId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found"));
        topicRepository.delete(topic);
    }

    private String cleanNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            return null;
        }
        return notes.trim();
    }

    private NoteFile saveNoteFile(Topic topic, MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only PDF, JPG, PNG, and WEBP files are allowed");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "notes-file" : file.getOriginalFilename());
        String extension = extensionFrom(originalFileName);
        String storedFileName = UUID.randomUUID() + extension;
        Path target = uploadDirectory.resolve(storedFileName).normalize();

        try {
            file.transferTo(target);
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save file");
        }

        NoteFile noteFile = new NoteFile();
        noteFile.setOriginalFileName(originalFileName);
        noteFile.setStoredFileName(storedFileName);
        noteFile.setContentType(contentType);
        noteFile.setSizeBytes(file.getSize());
        topic.addNoteFile(noteFile);
        return noteFileRepository.save(noteFile);
    }

    private String extensionFrom(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }
        return fileName.substring(dotIndex);
    }

    public record NoteFileDownload(NoteFile noteFile, Resource resource) {
    }
}
