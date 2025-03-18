package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.model.playlist.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT DISTINCT p FROM Playlist p LEFT JOIN FETCH p.songs")
    List<Playlist> findAll();

    List<Playlist> getPlaylistsById(Long id);

    Playlist getPlaylistById(Long id);
}
