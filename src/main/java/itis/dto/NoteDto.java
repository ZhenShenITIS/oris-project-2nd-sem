package itis.dto;

import lombok.Builder;

@Builder
public record NoteDto (
        Long id,
        String title,
        String content,
        String createdAt,
        Boolean isPublic,
        String authorName

){

}
