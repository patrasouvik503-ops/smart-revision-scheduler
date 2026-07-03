package com.smartrevision.scheduler.controller;

import com.smartrevision.scheduler.api.AddTopicRequest;
import com.smartrevision.scheduler.api.NoteFileResponse;
import com.smartrevision.scheduler.api.TopicResponse;
import com.smartrevision.scheduler.auth.AuthInterceptor;
import com.smartrevision.scheduler.auth.CurrentUser;
import com.smartrevision.scheduler.service.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TopicResponse addTopic(@Valid @RequestBody AddTopicRequest request, HttpServletRequest httpRequest) {
        CurrentUser user = (CurrentUser) httpRequest.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
        return topicService.addTopic(user.id(), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Long id, HttpServletRequest httpRequest) {
        CurrentUser user = (CurrentUser) httpRequest.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
        topicService.deleteTopic(user.id(), id);
    }

    @PostMapping(value = "/{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<NoteFileResponse> addNoteFiles(
            @PathVariable Long id,
            @RequestPart("files") List<MultipartFile> files,
            HttpServletRequest httpRequest
    ) {
        CurrentUser user = (CurrentUser) httpRequest.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
        return topicService.addNoteFiles(user.id(), id, files);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<Resource> downloadNoteFile(@PathVariable Long fileId, HttpServletRequest httpRequest) {
        CurrentUser user = (CurrentUser) httpRequest.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
        var download = topicService.loadNoteFile(user.id(), fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(download.noteFile().getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + download.noteFile().getOriginalFileName() + "\"")
                .body(download.resource());
    }
}
