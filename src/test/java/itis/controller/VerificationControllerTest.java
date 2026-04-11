package itis.controller;

import itis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VerificationController.class)
class VerificationControllerTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void verifyAccount_validCode_setsSuccessTrueInModel() throws Exception {
        given(userService.verifyUser("valid-code")).willReturn(true);

        mockMvc.perform(get("/verification")
                        .param("code", "valid-code")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("verification_result"))
                .andExpect(model().attribute("success", true));
    }

    @Test
    void verifyAccount_invalidCode_setsSuccessFalseInModel() throws Exception {
        given(userService.verifyUser("bad-code")).willReturn(false);

        mockMvc.perform(get("/verification")
                        .param("code", "bad-code")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("verification_result"))
                .andExpect(model().attribute("success", false));
    }
}
