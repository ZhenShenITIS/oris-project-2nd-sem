package itis.mapper;

import itis.dto.UserDto;
import itis.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void map_userDtoToUser_mapsIdAndName() {
        UserDto dto = new UserDto(1L, "alice");

        User user = UserMapper.map(dto);

        assertEquals(1L, user.getId());
        assertEquals("alice", user.getName());
    }

    @Test
    void map_userToUserDto_mapsIdAndName() {
        User user = User.builder().id(2L).name("bob").build();

        UserDto dto = UserMapper.map(user);

        assertEquals(2L, dto.id());
        assertEquals("bob", dto.name());
    }

    @Test
    void map_userDtoWithNullId_mapsNullId() {
        UserDto dto = new UserDto(null, "charlie");

        User user = UserMapper.map(dto);

        assertNull(user.getId());
        assertEquals("charlie", user.getName());
    }
}
