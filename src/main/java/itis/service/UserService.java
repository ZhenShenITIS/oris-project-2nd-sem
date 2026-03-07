package itis.service;

import itis.dto.UserDto;
import itis.mapper.UserMapper;
import itis.model.User;
import itis.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserJpaRepository userJpaRepository;

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


}
