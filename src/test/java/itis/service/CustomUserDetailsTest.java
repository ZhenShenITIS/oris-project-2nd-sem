package itis.service;

import itis.model.Role;
import itis.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void getAuthorities_returnsMappedRoleNames() {
        Role role = Role.builder().id(1L).name("USER").build();
        User user = User.builder().id(1L).name("alice").password("secret").roles(List.of(role)).build();

        CustomUserDetails details = new CustomUserDetails(user);
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals("USER", authorities.iterator().next().getAuthority());
    }

    @Test
    void getAuthorities_multipleRoles() {
        Role userRole = Role.builder().id(1L).name("USER").build();
        Role adminRole = Role.builder().id(2L).name("ADMIN").build();
        User user = User.builder().id(1L).name("admin").password("pass")
                .roles(List.of(userRole, adminRole)).build();

        CustomUserDetails details = new CustomUserDetails(user);
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();

        assertEquals(2, authorities.size());
    }

    @Test
    void getPassword_returnsUserPassword() {
        User user = User.builder().id(1L).name("alice").password("encoded_pass").roles(List.of()).build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("encoded_pass", details.getPassword());
    }

    @Test
    void getUsername_returnsUserName() {
        User user = User.builder().id(1L).name("alice").password("pass").roles(List.of()).build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("alice", details.getUsername());
    }
}
