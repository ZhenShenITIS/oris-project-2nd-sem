package itis.controller;

import itis.model.Note;
import itis.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public String getMyNotes(Model model, Principal principal) {
        List<Note> notes = noteService.getMyNotes(principal.getName());
        model.addAttribute("notes", notes);
        return "notes";
    }

    @GetMapping("/public")
    public String getPublicNotes(Model model) {
        model.addAttribute("notes", noteService.getPublicNotes());
        return "public_notes";
    }

    @GetMapping("/create")
    public String showCreateForm() {
        return "note_form";
    }

    @PostMapping("/create")
    public String createNote(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "title") String content,
            @RequestParam(name = "isPublic", defaultValue = "false") boolean isPublic,
            Principal principal
    ) {
        noteService.createNote(title, content, isPublic, principal.getName());
        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable(name = "id") Long id, Model model, Principal principal) {
        Note note = noteService.getNoteForEdit(id, principal.getName());
        model.addAttribute("note", note);
        return "note_form";
    }

    @PostMapping("/{id}/edit")
    public String updateNote(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "title") String title,
            @RequestParam(name =  "content") String content,
            @RequestParam(name = "isPublic", defaultValue = "false") boolean isPublic,
            Principal principal
    ) {
        noteService.updateNote(id, title, content, isPublic, principal.getName());
        return "redirect:/notes";
    }

    @PostMapping("/{id}/delete")
    public String deleteNote(@PathVariable (name = "id") Long id, Principal principal) {
        noteService.deleteNote(id, principal.getName());
        return "redirect:/notes";
    }
}
