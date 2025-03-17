package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findById(Long id);
    @Query("SELECT new com.codecool.tavirutyutyu.zsomlexd.model.Song(s.id, s.title, s.author, s.cover, s.length, s.numberOfLikes, s.reShare) FROM Song s")
    List<Song> findAllWithoutAudio();


    @Query("SELECT DISTINCT s FROM Song s WHERE LOWER(s.title) iLIKE LOWER(concat('%', :searchTerm, '%')) OR LOWER(s.author.name) iLIKE LOWER(concat('%', :searchTerm, '%'))")
    List<Song> findDistinctByTitleOrAuthorContainingIgnoreCase(@Param("searchTerm") String searchTerm);
}
