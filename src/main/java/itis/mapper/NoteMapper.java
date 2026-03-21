package itis.mapper;

import itis.dto.NoteDto;
import itis.model.Note;
import itis.util.DateFormatterUtil;

public class NoteMapper {

    public static NoteDto toDto(Note note) {
        return NoteDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(DateFormatterUtil.format(note.getCreatedAt()))
                .isPublic(note.getIsPublic())
                .authorName(note.getAuthor() != null ? note.getAuthor().getName() : "Неизвестен")
                .build();
    }
}
