package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongUploadDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public List<SongDataDTO> getAllSongs() {
        try{
            List<Song> songs = songRepository.findAll();
            return songs.stream().map(this::convertSongToSongDataDTO).toList();
        }catch (Exception e){
            throw new RuntimeException("Songs not found");
        }

    }

    private SongDTO convertSongToSongDTO(Song song) {
        String audioBase64 = Base64.getEncoder().encodeToString(song.getAudio());
        String albumCoverBase64 = Base64.getEncoder().encodeToString(song.getCover());
        return new SongDTO(song.getTitle(), audioBase64, albumCoverBase64,song.getLength(), song.getNumberOfLikes(), song.getReShare());
    }

    public void deleteSongById(Long id) {
        songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + id));

        songRepository.deleteById(id);
    }

    public Set<SongDataDTO> searchSong(String searchString) {
        try{
            List<Song> songsByTitle = songRepository.findDistinctByTitleOrAuthorContainingIgnoreCase(searchString);
            Set<SongDataDTO> songDataDTOList = new HashSet<>();
            songsByTitle.forEach(song -> songDataDTOList.add(convertSongToSongDataDTO(song)));
            return songDataDTOList;
        }catch (Exception e){
            throw new RuntimeException("Songs not found");
        }
    }

    private SongDataDTO convertSongToSongDataDTO(Song song) {
        String coverBase64 = Base64.getEncoder().encodeToString(song.getCover());
        return new SongDataDTO(song.getTitle(), song.getAuthor().getName(),coverBase64 ,song.getLength(), song.getNumberOfLikes(), song.getReShare(), song.getId());
    }

    @Transactional
    public SongDTO addSong(SongUploadDTO songUploadDTO, MultipartFile file, MultipartFile cover) {
        try {
            if (file.isEmpty() || cover.isEmpty()) {
                throw new IllegalArgumentException("Audio or cover file cannot be empty");
            }
            if (!isImageFile(cover)) {
                throw new IllegalArgumentException("Cover file must be an image");
            }
            if (!isAudioFile(file)) {
                throw new IllegalArgumentException("Audio file must be in MP3 format");
            }

            logger.info("Author: " + songUploadDTO.author());
            User author = userRepository.findByName(songUploadDTO.author());
            logger.info("Length:" + getAudioDuration(file));

            Song song = new Song();
            song.setTitle(songUploadDTO.title());
            song.setAuthor(author);
            song.setLength(getAudioDuration(file));
            song.setAudio(file.getBytes());
            song.setCover(cover.getBytes());

            logger.info("Song length:" + song.getLength());

            Song savedSong = songRepository.save(song);
            return convertSongToSongDTO(savedSong);
        } catch (IOException e) {
            logger.error("File upload error: {}", e.getMessage());
            throw new RuntimeException("File upload error");
        }
    }

    private double getAudioDuration(MultipartFile file) throws IOException {
        Metadata metadata = new Metadata();
        ContentHandler contentHandler = new BodyContentHandler();
        ParseContext parseContext = new ParseContext();
        try(InputStream input = file.getInputStream()){
            String fileType = file.getContentType();

            if(fileType != null && fileType.equals("audio/mpeg")){
                Mp3Parser mp3Parser = new Mp3Parser();
                mp3Parser.parse(input, contentHandler, metadata, parseContext);
            }else{
                AudioParser parser = new AudioParser();
                parser.parse(input, contentHandler, metadata, parseContext);
            }

            String duration = metadata.get("xmpDM:duration");
            logger.info(duration);
            return duration != null ? Double.parseDouble(duration): 0;
        } catch (TikaException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getAudioStreamById(Long id) {
        try{
            Optional<Song> song = songRepository.findById(id);
            if (song.isPresent() && song.get().getAudio() != null) {
                return new ByteArrayInputStream(song.get().getAudio()); // Streaming directly from memory
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException("Song not found with id: " + id);
        }
    }

    public SongDataDTO getSongDetailsById(Long id) {
        try{
            Song song = songRepository.findById(id).orElseThrow(() -> new RuntimeException("Song not found"));

            return convertSongToSongDataDTO(song);
        }catch (Exception e){
            throw new RuntimeException("Song not found with id: " + id);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private boolean isAudioFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("audio/mpeg") || contentType.equals("audio/mp3"));
    }
}
