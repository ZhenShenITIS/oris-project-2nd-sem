package itis.service;

import itis.model.Note;
import itis.model.User;
import itis.repository.NoteRepository;
import itis.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserJpaRepository userJpaRepository;

    public List<Note> getMyNotes(String username) {
        User user = getUserByUsername(username);
        return noteRepository.findByAuthor(user);
    }

    public List<Note> getPublicNotes() {
        return noteRepository.findByIsPublicTrue();
    }

    @Transactional
    public void createNote(String title, String content, boolean isPublic, String username) {
        User user = getUserByUsername(username);
        Note note = Note.builder()
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .createdAt(Instant.now())
                .author(user)
                .build();
        noteRepository.save(note);
    }

    public Note getNoteForEdit(Long id, String username) {
        Note note = getNoteOrThrow(id);
        checkOwnership(note, username);
        return note;
    }

    @Transactional
    public void updateNote(Long id, String title, String content, boolean isPublic, String username) {
        Note note = getNoteOrThrow(id);
        checkOwnership(note, username);
        note.setTitle(title);
        note.setContent(content);
        note.setIsPublic(isPublic);
        noteRepository.save(note);
    }

    @Transactional
    public void deleteNote(Long id, String username) {
        Note note = getNoteOrThrow(id);
        checkOwnership(note, username);
        noteRepository.delete(note);
    }


    private Note getNoteOrThrow(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заметка не найдена: " + id));
    }

    private void checkOwnership(Note note, String username) {
        if (!note.getAuthor().getName().equals(username)) {
            throw new SecurityException("Нет доступа к этой заметке");
        }
    }

    private User getUserByUsername(String username) {
        return userJpaRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
    }
}
