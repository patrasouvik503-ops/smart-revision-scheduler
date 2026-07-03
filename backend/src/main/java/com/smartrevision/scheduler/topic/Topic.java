package com.smartrevision.scheduler.topic;

import com.smartrevision.scheduler.revision.Revision;
import com.smartrevision.scheduler.note.NoteFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    @Column(nullable = false)
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(name = "date_learned", nullable = false)
    private LocalDate dateLearned;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "date_added", nullable = false)
    private Instant dateAdded = Instant.now();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Revision> revisions = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoteFile> noteFiles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDate getDateLearned() {
        return dateLearned;
    }

    public void setDateLearned(LocalDate dateLearned) {
        this.dateLearned = dateLearned;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getDateAdded() {
        return dateAdded;
    }

    public List<Revision> getRevisions() {
        return revisions;
    }

    public List<NoteFile> getNoteFiles() {
        return noteFiles;
    }

    public void addNoteFile(NoteFile noteFile) {
        noteFiles.add(noteFile);
        noteFile.setTopic(this);
    }

    public void addRevision(Revision revision) {
        revisions.add(revision);
        revision.setTopic(this);
    }
}
