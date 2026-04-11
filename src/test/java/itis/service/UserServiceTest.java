package itis.service;

import itis.dto.UserDto;
import itis.model.Role;
import itis.model.User;
import itis.repository.RoleRepository;
import itis.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserJpaRepository userJpaRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private SenderMailService senderMailService;

    @InjectMocks private UserService userService;

    @Test
    void findAll_returnsMappedDtos() {
        User user = User.builder().id(1L).name("alice").build();
        given(userJpaRepository.findAll()).willReturn(List.of(user));

        List<UserDto> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("alice", result.get(0).name());
    }

    @Test
    void findById_found_returnsDto() {
        User user = User.builder().id(1L).name("alice").build();
        given(userJpaRepository.findById(1L)).willReturn(Optional.of(user));

        Optional<UserDto> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("alice", result.get().name());
    }

    @Test
    void findById_notFound_returnsEmpty() {
        given(userJpaRepository.findById(99L)).willReturn(Optional.empty());

        Optional<UserDto> result = userService.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void create_setsIdToNullBeforeSaving() {
        UserDto dto = new UserDto(100L, "bob");
        User savedUser = User.builder().id(1L).name("bob").build();
        given(userJpaRepository.save(any())).willReturn(savedUser);

        UserDto result = userService.create(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userJpaRepository).save(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("bob", result.name());
    }

    @Test
    void update_savesAndReturnsDto() {
        UserDto dto = new UserDto(1L, "alice");
        User savedUser = User.builder().id(1L).name("alice").build();
        given(userJpaRepository.save(any())).willReturn(savedUser);

        UserDto result = userService.update(dto);

        assertEquals(1L, result.id());
        assertEquals("alice", result.name());
    }

    @Test
    void deleteById_callsRepositoryDeleteById() {
        userService.deleteById(1L);
        verify(userJpaRepository).deleteById(1L);
    }

    @Test
    void registerNewUser_success_savesUserAndSendsEmail() {
        Role role = Role.builder().id(1L).name("USER").build();
        given(userJpaRepository.findByName("alice")).willReturn(Optional.empty());
        given(roleRepository.findByName("USER")).willReturn(Optional.of(role));
        given(passwordEncoder.encode("password")).willReturn("encoded");
        given(userJpaRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> userService.registerNewUser("alice", "password", "alice@example.com"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userJpaRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("alice", saved.getName());
        assertEquals("alice@example.com", saved.getEmail());
        assertEquals("encoded", saved.getPassword());
        assertNotNull(saved.getVerificationCode());
        verify(senderMailService).sendVerificationEmail(any());
    }

    @Test
    void registerNewUser_userAlreadyExists_throwsIllegalArgumentException() {
        given(userJpaRepository.findByName("alice")).willReturn(
                Optional.of(User.builder().id(1L).name("alice").build()));

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerNewUser("alice", "password", "alice@example.com"));
    }

    @Test
    void registerNewUser_roleNotFound_throwsRuntimeException() {
        given(userJpaRepository.findByName("newuser")).willReturn(Optional.empty());
        given(roleRepository.findByName("USER")).willReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.registerNewUser("newuser", "password", "new@example.com"));
    }

    @Test
    void verifyUser_validCode_verifiesUserAndReturnsTrue() {
        User user = User.builder().id(1L).name("alice").isVerified(false)
                .verificationCode("valid-code").build();
        given(userJpaRepository.findByVerificationCode("valid-code")).willReturn(Optional.of(user));
        given(userJpaRepository.save(any())).willReturn(user);

        boolean result = userService.verifyUser("valid-code");

        assertTrue(result);
        assertTrue(user.getIsVerified());
        assertNull(user.getVerificationCode());
        verify(userJpaRepository).save(user);
    }

    @Test
    void verifyUser_invalidCode_returnsFalse() {
        given(userJpaRepository.findByVerificationCode("bad-code")).willReturn(Optional.empty());

        boolean result = userService.verifyUser("bad-code");

        assertFalse(result);
    }

    @Test
    void verifyUser_alreadyVerified_returnsFalse() {
        User user = User.builder().id(1L).name("alice").isVerified(true)
                .verificationCode("old-code").build();
        given(userJpaRepository.findByVerificationCode("old-code")).willReturn(Optional.of(user));

        boolean result = userService.verifyUser("old-code");

        assertFalse(result);
    }
}
