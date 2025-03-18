package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.model.playlist.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT new com.codecool.tavirutyutyu.zsomlexd.model.playlist.Playlist(p.id, p.title, p.user) FROM Playlist p")
    List<Playlist> findAll();

    Playlist getPlaylistById(Long id);
}
