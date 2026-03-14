package itis.service;

import itis.dto.UserDto;
import itis.mapper.UserMapper;
import itis.model.Role;
import itis.model.User;
import itis.repository.RoleRepository;
import itis.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserJpaRepository userJpaRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
    public void registerNewUser(String username, String rawPassword) {
        if (userJpaRepository.findByName(username).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Роль USER не найдена в БД"));

        User user = User.builder()
                .name(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(List.of(userRole))
                .build();

        userJpaRepository.save(user);
    }
}
