package itis.mapper;

import itis.dto.UserDto;
import itis.model.User;

public class UserMapper {
    public static User map(UserDto userDto) {
        return User
                .builder()
                .id(userDto.id())
                .name(userDto.name())
                .build();
    }

    public static UserDto map(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
