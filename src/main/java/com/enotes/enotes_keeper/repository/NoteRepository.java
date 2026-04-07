package com.enotes.enotes_keeper.repository;

import com.enotes.enotes_keeper.entity.Note;
import com.enotes.enotes_keeper.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // Pagination
    Page<Note> findByUser(User user, Pageable pageable);

    // 🔥 Search (Title + Content)
    Page<Note> findByUserAndTitleContainingIgnoreCaseOrUserAndContentContainingIgnoreCase(
            User user1, String title,
            User user2, String content,
            Pageable pageable
    );
}