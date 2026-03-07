package itis.controller;

import itis.dto.UserDto;
import itis.model.User;
import itis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDto> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        Optional<UserDto> userDto = userService.findById(id);
        return userDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.create(userDto);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto updatedUser = userService.update(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
