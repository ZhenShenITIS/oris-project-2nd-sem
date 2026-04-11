package itis.service;

import itis.dto.NoteDto;
import itis.model.Note;
import itis.model.User;
import itis.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getAllNotes_returnsMappedDtos() {
        User author = User.builder().id(1L).name("alice").build();
        Note note = Note.builder()
                .id(1L).title("My Note").content("Content")
                .createdAt(Instant.now()).isPublic(true).author(author)
                .build();
        given(noteRepository.findAll()).willReturn(List.of(note));

        List<NoteDto> result = adminService.getAllNotes();

        assertEquals(1, result.size());
        assertEquals("My Note", result.get(0).title());
        assertEquals("Content", result.get(0).content());
        assertEquals("alice", result.get(0).authorName());
        assertTrue(result.get(0).isPublic());
    }

    @Test
    void getAllNotes_emptyRepository_returnsEmptyList() {
        given(noteRepository.findAll()).willReturn(List.of());

        List<NoteDto> result = adminService.getAllNotes();

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteNote_callsRepositoryDeleteById() {
        adminService.deleteNote(42L);
        verify(noteRepository).deleteById(42L);
    }
}
