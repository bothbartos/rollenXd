package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongUploadDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(SongService.class);

    @Autowired
    public SongService(SongRepository songRepository, UserRepository userRepository) {
        this.songRepository = songRepository;
        this.userRepository = userRepository;
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

    public Set<SongDataDTO> searchSong(String searchString) {
        Set<Song> songsByTitle = songRepository.findByTitleLikeIgnoreCase(searchString);
        Set<Song> songByArtist = songRepository.findAllByAuthorName(searchString);
        Set<SongDataDTO> songDataDTOList = new HashSet<>();
        songsByTitle.forEach(song -> songDataDTOList.add(convertSongToSongDataDTO(song)));
        songByArtist.forEach(song -> songDataDTOList.add(convertSongToSongDataDTO(song)));
        logger.info("Songs found: " + songsByTitle.size());
        logger.info("Songs found: " + songByArtist.size());
        return songDataDTOList;
    }

    private SongDataDTO convertSongToSongDataDTO(Song song) {
        return new SongDataDTO(song.getTitle(), song.getAuthor().getName(), song.getLength(), song.getNumberOfLikes(), song.getReShare());
    }

    @Transactional
    public SongDTO addSong(SongUploadDTO songUploadDTO, MultipartFile file) {
        try{
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Audio file cannot be empty");
            }

            logger.info("Author: " + songUploadDTO.author());
            User author = userRepository.findByName(songUploadDTO.author());

            Song song = new Song();
            song.setTitle(songUploadDTO.title());
            song.setAuthor(author);
            song.setLength(songUploadDTO.length());
            song.setAudio(file.getBytes());

            Song savedSong = songRepository.save(song);
            return convertSongToSongDTO(savedSong);
        }catch (IOException e){
            logger.error("File upload error: {}", e.getMessage());
            throw new RuntimeException("File upload error");
        }
    }
}
