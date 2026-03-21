package itis.service;

import itis.dto.NoteDto;

import itis.mapper.NoteMapper;
import itis.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final NoteRepository noteRepository;

    // В идеале из БД вытаскивать сразу в виде ДТО и с пагинацией
    public List<NoteDto> getAllNotes() {
        return noteRepository.findAll().stream()
                .map(NoteMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
