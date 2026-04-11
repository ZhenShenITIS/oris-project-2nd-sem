package itis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import itis.config.SecurityConfig;
import itis.dto.UserDto;
import itis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllUsers() throws Exception {
        UserDto userDto = new UserDto(1L, "yser");
        given(userService.findAll()).willReturn(List.of(userDto));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(user("user").roles("USER"))
                ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("yser"));
    }

    @Test
    public void testGetAllUsers_emptyList() throws Exception {
        given(userService.findAll()).willReturn(List.of());

        mockMvc.perform(get("/users")
                        .with(user("user").roles("USER"))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetUser_found() throws Exception {
        UserDto userDto = new UserDto(1L, "alice");
        given(userService.findById(1L)).willReturn(Optional.of(userDto));

        mockMvc.perform(get("/user/1")
                        .with(user("user").roles("USER"))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("alice"));
    }

    @Test
    public void testGetUser_notFound() throws Exception {
        given(userService.findById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/user/99")
                        .with(user("user").roles("USER"))
                ).andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDto input = new UserDto(null, "bob");
        UserDto created = new UserDto(2L, "bob");
        given(userService.create(any())).willReturn(created);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(input))
                        .with(user("user").roles("USER"))
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("bob"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDto input = new UserDto(1L, "updated");
        UserDto updated = new UserDto(1L, "updated");
        given(userService.update(any())).willReturn(updated);

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(input))
                        .with(user("user").roles("USER"))
                        .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("updated"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/user/1")
                        .with(user("user").roles("USER"))
                        .with(csrf())
                ).andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllUsers_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }
}
