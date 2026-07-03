package com.smartrevision.scheduler.controller;

import com.smartrevision.scheduler.api.DashboardResponse;
import com.smartrevision.scheduler.api.RevisionResponse;
import com.smartrevision.scheduler.api.StatisticsResponse;
import com.smartrevision.scheduler.auth.AuthInterceptor;
import com.smartrevision.scheduler.auth.CurrentUser;
import com.smartrevision.scheduler.service.RevisionService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RevisionController {

    private final RevisionService revisionService;

    public RevisionController(RevisionService revisionService) {
        this.revisionService = revisionService;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(HttpServletRequest request) {
        return revisionService.dashboard(currentUser(request).id());
    }

    @GetMapping("/statistics")
    public StatisticsResponse statistics(HttpServletRequest request) {
        return revisionService.statistics(currentUser(request).id());
    }

    @GetMapping("/revisions/today")
    public List<RevisionResponse> today(HttpServletRequest request) {
        return revisionService.today(currentUser(request).id());
    }

    @GetMapping("/revisions/calendar")
    public List<RevisionResponse> calendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request
    ) {
        return revisionService.calendar(currentUser(request).id(), from, to);
    }

    @PatchMapping("/revisions/{id}/complete")
    public RevisionResponse complete(@PathVariable Long id, HttpServletRequest request) {
        return revisionService.complete(currentUser(request).id(), id);
    }

    private CurrentUser currentUser(HttpServletRequest request) {
        return (CurrentUser) request.getAttribute(AuthInterceptor.CURRENT_USER_ATTRIBUTE);
    }
}
