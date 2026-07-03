package com.smartrevision.scheduler.note;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteFileRepository extends JpaRepository<NoteFile, Long> {

    Optional<NoteFile> findByIdAndTopicUserId(Long id, Long userId);
}
