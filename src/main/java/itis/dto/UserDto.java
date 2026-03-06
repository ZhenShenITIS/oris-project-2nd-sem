package itis.dto;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String name
) {
}
