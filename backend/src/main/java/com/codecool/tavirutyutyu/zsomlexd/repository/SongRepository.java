package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findById(Long id);
    Optional<Song> findByTitle(String title);
    List<Song> findAll();

    List<Song> findAllByTitle(String searchString);

    @Query("SELECT s from Song s where lower(s.author.name) like lower(concat('%', :name, '%'))")
    List<Song> findAllByAuthorName(@Param("name") String name);
}
