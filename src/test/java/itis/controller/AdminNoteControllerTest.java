package itis.controller;

import itis.config.SecurityConfig;
import itis.dto.NoteDto;
import itis.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminNoteController.class)
@Import(SecurityConfig.class)
class AdminNoteControllerTest {

    @MockitoBean
    private AdminService adminService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllNotes_asAdmin_returnsNoteList() throws Exception {
        NoteDto noteDto = NoteDto.builder()
                .id(1L).title("Admin Note").content("Content")
                .createdAt("01.01.2024 12:00").isPublic(true).authorName("alice")
                .build();
        given(adminService.getAllNotes()).willReturn(List.of(noteDto));

        mockMvc.perform(get("/admin/notes")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Admin Note"))
                .andExpect(jsonPath("$[0].authorName").value("alice"));
    }

    @Test
    void getAllNotes_asAdmin_emptyList() throws Exception {
        given(adminService.getAllNotes()).willReturn(List.of());

        mockMvc.perform(get("/admin/notes")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void deleteNote_asAdmin_returnsNoContent() throws Exception {
        doNothing().when(adminService).deleteNote(1L);

        // CSRF is disabled for /admin/**, so no .with(csrf()) needed
        mockMvc.perform(delete("/admin/notes/1")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllNotes_asUser_returnsForbidden() throws Exception {
        mockMvc.perform(get("/admin/notes")
                        .with(user("user").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllNotes_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin/notes"))
                .andExpect(status().isUnauthorized());
    }
}
