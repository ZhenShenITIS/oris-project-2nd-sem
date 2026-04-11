package itis.service;

import itis.model.Note;
import itis.model.User;
import itis.repository.NoteRepository;
import itis.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock private NoteRepository noteRepository;
    @Mock private UserJpaRepository userJpaRepository;

    @InjectMocks private NoteService noteService;

    private User buildUser(String name) {
        return User.builder().id(1L).name(name).build();
    }

    private Note buildNote(Long id, User author) {
        return Note.builder()
                .id(id).title("Title").content("Content")
                .createdAt(Instant.now()).isPublic(true).author(author)
                .build();
    }

    @Test
    void getMyNotes_returnsNotesForUser() {
        User user = buildUser("alice");
        Note note = buildNote(1L, user);
        given(userJpaRepository.findByName("alice")).willReturn(Optional.of(user));
        given(noteRepository.findByAuthor(user)).willReturn(List.of(note));

        List<Note> result = noteService.getMyNotes("alice");

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    void getMyNotes_userNotFound_throwsUsernameNotFoundException() {
        given(userJpaRepository.findByName("ghost")).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> noteService.getMyNotes("ghost"));
    }

    @Test
    void getPublicNotes_returnsPublicNotes() {
        User user = buildUser("alice");
        Note note = buildNote(1L, user);
        given(noteRepository.findByIsPublicTrue()).willReturn(List.of(note));

        List<Note> result = noteService.getPublicNotes();

        assertEquals(1, result.size());
    }

    @Test
    void createNote_savesNoteWithAllFields() {
        User user = buildUser("alice");
        given(userJpaRepository.findByName("alice")).willReturn(Optional.of(user));

        noteService.createNote("New Title", "New Content", true, "alice");

        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).save(captor.capture());
        Note saved = captor.getValue();
        assertEquals("New Title", saved.getTitle());
        assertEquals("New Content", saved.getContent());
        assertTrue(saved.getIsPublic());
        assertEquals(user, saved.getAuthor());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void getNoteForEdit_success_returnsNote() {
        User user = buildUser("alice");
        Note note = buildNote(1L, user);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));

        Note result = noteService.getNoteForEdit(1L, "alice");

        assertEquals(1L, result.getId());
    }

    @Test
    void getNoteForEdit_wrongOwner_throwsSecurityException() {
        User owner = buildUser("alice");
        Note note = buildNote(1L, owner);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));

        assertThrows(SecurityException.class, () -> noteService.getNoteForEdit(1L, "bob"));
    }

    @Test
    void getNoteForEdit_noteNotFound_throwsIllegalArgumentException() {
        given(noteRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> noteService.getNoteForEdit(99L, "alice"));
    }

    @Test
    void updateNote_success_updatesAllFields() {
        User user = buildUser("alice");
        Note note = buildNote(1L, user);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));

        noteService.updateNote(1L, "Updated Title", "Updated Content", false, "alice");

        verify(noteRepository).save(note);
        assertEquals("Updated Title", note.getTitle());
        assertEquals("Updated Content", note.getContent());
        assertFalse(note.getIsPublic());
    }

    @Test
    void updateNote_wrongOwner_throwsSecurityException() {
        User owner = buildUser("alice");
        Note note = buildNote(1L, owner);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));

        assertThrows(SecurityException.class,
                () -> noteService.updateNote(1L, "T", "C", false, "bob"));
    }

    @Test
    void deleteNote_success_deletesNote() {
        User user = buildUser("alice");
        Note note = buildNote(1L, user);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));

        noteService.deleteNote(1L, "alice");

        verify(noteRepository).delete(note);
    }

    @Test
    void deleteNote_wrongOwner_throwsSecurityException() {
        User owner = buildUser("alice");
        Note note = buildNote(1L, owner);
        given(noteRepository.findById(1L)).willReturn(Optional.of(note));

        assertThrows(SecurityException.class, () -> noteService.deleteNote(1L, "bob"));
    }
}
