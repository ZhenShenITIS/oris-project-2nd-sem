package itis.controller;

import itis.config.SecurityConfig;
import itis.service.HelloService;
import itis.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HelloController.class)
@Import(SecurityConfig.class)
class HelloControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private HelloService helloService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void hello_withName_returnsGreeting() throws Exception {
        given(helloService.sayHello("World")).willReturn("Hello, World");

        mockMvc.perform(get("/hello")
                        .param("name", "World")
                        .with(user("alice").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World"));
    }

    @Test
    void hello_withoutName_callsServiceWithNull() throws Exception {
        given(helloService.sayHello(null)).willReturn("Hello, null");

        mockMvc.perform(get("/hello")
                        .with(user("alice").authorities(new SimpleGrantedAuthority("USER"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, null"));
    }

    @Test
    void hello_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isUnauthorized());
    }
}
