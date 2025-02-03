package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findById(Long id);
    Optional<Song> findByTitle(String title);
    List<Song> findAll();
}
