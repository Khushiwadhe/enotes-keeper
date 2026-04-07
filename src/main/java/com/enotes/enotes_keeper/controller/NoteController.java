package com.enotes.enotes_keeper.controller;

import com.enotes.enotes_keeper.entity.Note;
import com.enotes.enotes_keeper.entity.User;
import com.enotes.enotes_keeper.repository.NoteRepository;
import com.enotes.enotes_keeper.repository.UserRepository;
import com.enotes.enotes_keeper.service.NoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import java.security.Principal;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    // ✅ SHOW NOTES (PAGINATION + SEARCH + USERNAME)
    @GetMapping("/notes")
    public String notesPage(
            Model model,
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword
    ) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email).get();

        Page<Note> notePage;

        if (keyword != null && !keyword.isEmpty()) {
            notePage = noteService.searchNotes(user, keyword, page, 5);
        } else {
            notePage = noteService.getNotes(user, page, 5);
        }

        model.addAttribute("notes", notePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", notePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("username", user.getEmail());

        return "notes";
    }

    // SAVE NOTE
    @PostMapping("/saveNote")
    public String saveNote(@ModelAttribute Note note, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email).get();

        note.setUser(user);
        noteService.saveNote(note);

        return "redirect:/notes";
    }

    // EDIT PAGE
    @GetMapping("/edit/{id}")
    public String editNote(@PathVariable Long id, Model model, Principal principal) {

        Note note = noteRepository.findById(id).get();

        if (!note.getUser().getEmail().equals(principal.getName())) {
            return "redirect:/notes";
        }

        model.addAttribute("note", note);
        return "edit_note";
    }

    // UPDATE NOTE
    @PostMapping("/updateNote")
    public String updateNote(@ModelAttribute Note note, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email).get();

        note.setUser(user);
        noteService.saveNote(note);

        return "redirect:/notes";
    }

    // DELETE NOTE
    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, Principal principal) {

        Note note = noteRepository.findById(id).get();

        if (!note.getUser().getEmail().equals(principal.getName())) {
            return "redirect:/notes";
        }

        noteRepository.deleteById(id);

        return "redirect:/notes";
    }
}