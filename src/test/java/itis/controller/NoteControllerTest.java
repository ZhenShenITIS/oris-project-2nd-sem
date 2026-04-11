package itis.controller;

import itis.config.SecurityConfig;
import itis.model.Note;
import itis.model.User;
import itis.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@Import(SecurityConfig.class)
class NoteControllerTest {

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private MockMvc mockMvc;

    private Note buildNote() {
        User author = User.builder().id(1L).name("alice").build();
        return Note.builder()
                .id(1L).title("Test Note").content("Some content")
                .createdAt(Instant.parse("2024-01-15T12:00:00Z")).isPublic(true)
                .author(author)
                .build();
    }

    @Test
    void getMyNotes_authenticated_returnsNotesView() throws Exception {
        given(noteService.getMyNotes("alice")).willReturn(List.of(buildNote()));

        mockMvc.perform(get("/notes")
                        .with(user("alice").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("notes"))
                .andExpect(model().attributeExists("notes"));
    }

    @Test
    void getMyNotes_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/notes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPublicNotes_noAuth_returnsPublicNotesView() throws Exception {
        User author = User.builder().id(1L).name("alice").build();
        Note note = Note.builder()
                .id(1L).title("Public").content("Content")
                .createdAt(Instant.now()).isPublic(true).author(author)
                .build();
        given(noteService.getPublicNotes()).willReturn(List.of(note));

        mockMvc.perform(get("/notes/public"))
                .andExpect(status().isOk())
                .andExpect(view().name("public_notes"))
                .andExpect(model().attributeExists("notes"));
    }

    @Test
    void showCreateForm_returnsNoteFormView() throws Exception {
        mockMvc.perform(get("/notes/create")
                        .with(user("alice").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("note_form"));
    }

    @Test
    void createNote_success_redirectsToNotes() throws Exception {
        doNothing().when(noteService).createNote(anyString(), anyString(), anyBoolean(), eq("alice"));

        mockMvc.perform(post("/notes/create")
                        .param("title", "New Note")
                        .param("isPublic", "true")
                        .with(user("alice").authorities(new SimpleGrantedAuthority("USER")))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    void showEditForm_existingNote_returnsNoteFormWithModel() throws Exception {
        Note note = buildNote();
        given(noteService.getNoteForEdit(1L, "alice")).willReturn(note);

        mockMvc.perform(get("/notes/1/edit")
                        .with(user("alice").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("note_form"))
                .andExpect(model().attributeExists("note"));
    }

    @Test
    void updateNote_success_redirectsToNotes() throws Exception {
        doNothing().when(noteService).updateNote(eq(1L), anyString(), anyString(), anyBoolean(), eq("alice"));

        mockMvc.perform(post("/notes/1/edit")
                        .param("title", "Updated Title")
                        .param("content", "Updated Content")
                        .param("isPublic", "false")
                        .with(user("alice").authorities(new SimpleGrantedAuthority("USER")))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    void deleteNote_success_redirectsToNotes() throws Exception {
        doNothing().when(noteService).deleteNote(eq(1L), eq("alice"));

        mockMvc.perform(post("/notes/1/delete")
                        .with(user("alice").authorities(new SimpleGrantedAuthority("USER")))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notes"));
    }
}
