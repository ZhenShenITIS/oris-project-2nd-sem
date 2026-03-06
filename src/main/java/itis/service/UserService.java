package itis.service;

import itis.dto.UserDto;
import itis.mapper.UserMapper;
import itis.model.User;
import itis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::map).toList();
    }
}
