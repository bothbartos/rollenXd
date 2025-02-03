package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public byte[] getAudioById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        return song.getAudio();
    }
    public SongDTO getAudioByTitle(String title) {
        Optional<Song> songOptional = songRepository.findByTitle(title);
        if (songOptional.isEmpty()) {
            throw new RuntimeException("Song not found");
        }
        Song song = songOptional.get();
        System.out.println("Song retrieved: " + song.getTitle());

        if (song.getAudio() == null) {
            throw new RuntimeException("Audio data not available for the song");
        }
        return convertSongToSongDTO(song);
    }

    public List<SongDTO> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        return songs.stream().map(this::convertSongToSongDTO).toList();

    }
    private SongDTO convertSongToSongDTO(Song song) {
        String audioBase64 = Base64.getEncoder().encodeToString(song.getAudio());
        return new SongDTO(song.getTitle(), audioBase64, song.getLength(), song.getNumberOfLikes(), song.getReShare());
    }

    public void deleteSongById(Long id) {
        songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + id));

        songRepository.deleteById(id);
    }
}
