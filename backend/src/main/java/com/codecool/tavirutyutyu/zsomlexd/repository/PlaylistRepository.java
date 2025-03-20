package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.model.playlist.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query(value = "SELECT p.id, p.title, p.user_id, " +
            "(SELECT s.id FROM playlist_songs ps JOIN song s ON ps.song_id = s.id WHERE ps.playlist_id = p.id ORDER BY s.id ASC LIMIT 1) AS first_song_id " +
            "FROM playlist p", nativeQuery = true)
    List<Playlist> findAll();

    Playlist getPlaylistById(Long id);

    @Query("SELECT p FROM Playlist p WHERE p.user.name = :userName")
    List<Playlist> findAllByUserName(@Param("userName") String userName);

}
