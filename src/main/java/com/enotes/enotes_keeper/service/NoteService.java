package com.enotes.enotes_keeper.service;

import com.enotes.enotes_keeper.entity.Note;
import com.enotes.enotes_keeper.entity.User;
import com.enotes.enotes_keeper.repository.NoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    // SAVE NOTE
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    // PAGINATED NOTES
    public Page<Note> getNotes(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return noteRepository.findByUser(user, pageable);
    }

    // SEARCH (TITLE + CONTENT)
    public Page<Note> searchNotes(User user, String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return noteRepository
                .findByUserAndTitleContainingIgnoreCaseOrUserAndContentContainingIgnoreCase(
                        user, keyword, user, keyword, pageable
                );
    }
}