package itis.mapper;

import itis.dto.NoteDto;
import itis.model.Note;
import itis.model.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class NoteMapperTest {

    @Test
    void toDto_withAuthor_mapsAllFields() {
        User author = User.builder().id(1L).name("alice").build();
        Instant createdAt = Instant.parse("2024-06-01T09:00:00Z"); // 12:00 Moscow

        Note note = Note.builder()
                .id(10L)
                .title("Test Title")
                .content("Test Content")
                .createdAt(createdAt)
                .isPublic(true)
                .author(author)
                .build();

        NoteDto dto = NoteMapper.toDto(note);

        assertEquals(10L, dto.id());
        assertEquals("Test Title", dto.title());
        assertEquals("Test Content", dto.content());
        assertTrue(dto.isPublic());
        assertEquals("alice", dto.authorName());
        assertNotNull(dto.createdAt());
        assertEquals("01.06.2024 12:00", dto.createdAt());
    }

    @Test
    void toDto_withNullAuthor_usesDefaultName() {
        Note note = Note.builder()
                .id(2L)
                .title("No Author")
                .content("Content")
                .createdAt(Instant.now())
                .isPublic(false)
                .author(null)
                .build();

        NoteDto dto = NoteMapper.toDto(note);

        assertEquals("Неизвестен", dto.authorName());
    }

    @Test
    void toDto_withNullCreatedAt_returnsNullDate() {
        Note note = Note.builder()
                .id(3L)
                .title("Title")
                .content("Content")
                .createdAt(null)
                .isPublic(false)
                .author(null)
                .build();

        NoteDto dto = NoteMapper.toDto(note);

        assertNull(dto.createdAt());
    }
}
