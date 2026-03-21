package itis.controller;

import itis.dto.NoteDto;
import itis.model.Note;
import itis.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminNoteController {

    private final AdminService adminService;

    @GetMapping("/notes")
    public List<NoteDto> getAllNotes() {
        return adminService.getAllNotes();
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable(name = "id") Long id) {
        adminService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
