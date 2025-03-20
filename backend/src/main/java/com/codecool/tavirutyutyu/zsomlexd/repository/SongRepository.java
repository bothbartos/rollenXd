package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findById(Long id);

    @Query("SELECT new com.codecool.tavirutyutyu.zsomlexd.model.song.Song(s.id, s.title, s.author, s.cover, s.length, s.reShare) FROM Song s")
    List<Song> findAllWithoutAudio();

    @Query("SELECT s FROM Song s WHERE s.author.name = :authorName")
    List<Song> findAllWithoutAudioByAuthorName(@Param("authorName")String authorName);


    @Query("SELECT s.likedBy FROM Song s WHERE s.id = :songId")
    Set<User> findUsersWhoLikedSong(@Param("songId") Long songId);

    @Query("SELECT DISTINCT s FROM Song s WHERE LOWER(s.title) iLIKE LOWER(concat('%', :searchTerm, '%')) OR LOWER(s.author.name) iLIKE LOWER(concat('%', :searchTerm, '%'))")
    List<Song> findDistinctByTitleOrAuthorContainingIgnoreCase(@Param("searchTerm") String searchTerm);
}
