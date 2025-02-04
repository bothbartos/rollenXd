package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findById(Long id);
    Optional<Song> findByTitle(String title);
    List<Song> findAll();

    @Query("SELECT s FROM Song s where lower(s.title) ilike lower(concat('%', :title, '%'))")
    Set<Song> findByTitleLikeIgnoreCase(@Param("title") String title);

    @Query("SELECT s from Song s where lower(s.author.name) like lower(concat('%', :name, '%'))")
    Set<Song> findAllByAuthorName(@Param("name") String name);
}
