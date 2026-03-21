package itis.repository;

import itis.model.Note;
import itis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.author = :user")
    List<Note> findByAuthor(@Param("user") User user);

    @Query("SELECT n FROM Note n WHERE n.isPublic = true")
    List<Note> findByIsPublicTrue();
}
