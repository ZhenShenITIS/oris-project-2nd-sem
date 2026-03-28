package itis.service;

import itis.dto.CreateUserDto;
import itis.dto.UserDto;
import itis.mapper.UserMapper;
import itis.model.Role;
import itis.model.User;
import itis.properties.MailProperties;
import itis.repository.RoleRepository;
import itis.repository.UserJpaRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SenderMailService senderMailService;

    public List<UserDto> findAll() {
        return userJpaRepository.findAll().stream().map(UserMapper::map).toList();
    }

    public Optional<UserDto> findById(Long id) {
        return userJpaRepository.findById(id).map(UserMapper::map);
    }

    public UserDto create(UserDto userDto) {
        User user = UserMapper.map(userDto);
        user.setId(null);
        User savedUser = userJpaRepository.save(user);
        return UserMapper.map(savedUser);
    }

    public UserDto update (UserDto userDto) {
        User user = UserMapper.map(userDto);
        User updatedUser = userJpaRepository.save(user);
        return UserMapper.map(updatedUser);
    }

    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }


    @Transactional
    public void registerNewUser(String username, String rawPassword, String email) {
        if (userJpaRepository.findByName(username).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Роль USER не найдена в БД"));

        User user = User.builder()
                .name(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .verificationCode(UUID.randomUUID().toString())
                .roles(List.of(userRole))
                .build();

        userJpaRepository.save(user);
        senderMailService.sendVerificationEmail(user);
    }

    @Transactional
    public boolean verifyUser(String code) {
        Optional<User> userOptional = userJpaRepository.findByVerificationCode(code);

        if (userOptional.isPresent()) {
            log.info("Пользователь найден для верификации: {}", userOptional.get().getName());
            User user = userOptional.get();
            if (!user.getIsVerified()) {
                user.setIsVerified(true);
                user.setVerificationCode(null);
                userJpaRepository.save(user);
                return true;
            }
        }
        log.info("Пользователь не найден для верификации с кодом: {}", code);
        return false;
    }




    @Transactional
    public void registerNewUser(CreateUserDto createUserDto) {
        registerNewUser(createUserDto.email(), createUserDto.password(), createUserDto.email());
    }
}
