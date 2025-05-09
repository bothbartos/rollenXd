package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codecool.tavirutyutyu.zsomlexd.util.Utils.*;

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


    public List<SongDataDTO> getAllSongs() {
        try {
            List<Song> songs = songRepository.findAllWithoutAudio();
            User user = userRepository.findByName(getCurrentUser().getUsername());
            for (Song song : songs) {
                Set<User> likedBy = songRepository.findUsersWhoLikedSong(song.getId());
                song.setLikedBy(likedBy);
            }
            return songs.stream().map(song -> convertSongToSongDataDTO(song, user)).toList();
        } catch (Exception e) {
            throw new RuntimeException("Songs not found");
        }

    }



    private SongDTO convertSongToSongDTO(Song song, User user) {
        String audioBase64 = Base64.getEncoder().encodeToString(song.getAudio());
        String albumCoverBase64 = Base64.getEncoder().encodeToString(song.getCover());
        return new SongDTO(song.getTitle(), audioBase64, albumCoverBase64, song.getLength(), isLiked(song, user), song.getReShare());
    }

    public void deleteSongById(Long id) {
        songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + id));

        songRepository.deleteById(id);
    }

    public Set<SongDataDTO> searchSong(String searchString) {
        try {
            User user = userRepository.findByName(getCurrentUser().getUsername());
            List<Song> songsByTitle = songRepository.findDistinctByTitleOrAuthorContainingIgnoreCase(searchString);
            Set<SongDataDTO> songDataDTOList = new HashSet<>();
            songsByTitle.forEach(song -> songDataDTOList.add(convertSongToSongDataDTO(song, user)));
            return songDataDTOList;
        } catch (Exception e) {
            throw new RuntimeException("Songs not found! Message: "+e.getMessage());
        }
    }

    @Transactional
    public SongDTO addSong(String title, MultipartFile file, MultipartFile cover) {
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

            User author = userRepository.findByName(getCurrentUser().getUsername());

            Song song = new Song();
            song.setTitle(title);
            song.setAuthor(author);
            song.setLength(getAudioDuration(file));
            song.setAudio(file.getBytes());
            song.setCover(cover.getBytes());
            song.setLikedBy(new HashSet<>());

            logger.info("Song length:" + song.getLength());

            Song savedSong = songRepository.save(song);
            return convertSongToSongDTO(savedSong, author);
        } catch (IOException e) {
            logger.error("File upload error: {}", e.getMessage());
            throw new RuntimeException("File upload error");
        }
    }

    private double getAudioDuration(MultipartFile file) throws IOException {
        Metadata metadata = new Metadata();
        ContentHandler contentHandler = new BodyContentHandler();
        ParseContext parseContext = new ParseContext();
        try (InputStream input = file.getInputStream()) {
            String fileType = file.getContentType();

            if (fileType != null && fileType.equals("audio/mpeg")) {
                Mp3Parser mp3Parser = new Mp3Parser();
                mp3Parser.parse(input, contentHandler, metadata, parseContext);
            } else {
                AudioParser parser = new AudioParser();
                parser.parse(input, contentHandler, metadata, parseContext);
            }

            String duration = metadata.get("xmpDM:duration");
            logger.info("Duration: {}", duration);
            return duration != null ? Double.parseDouble(duration) : 0;
        } catch (TikaException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getAudioStreamById(Long id) {
        Song song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        return new ByteArrayInputStream(song.getAudio()); // Streaming directly from memory
    }

    public SongDataDTO getSongDetailsById(Long id) {
        User user = userRepository.findByName(getCurrentUser().getUsername());
        Song song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song not found"));
        return convertSongToSongDataDTO(song, user);
    }

    public SongDTO likeSong(Long id) {
        Song song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song not found"));
        User user = userRepository.findByName(getCurrentUser().getUsername());

        song.getLikedBy().add(user);
        song = songRepository.save(song);
        return convertSongToSongDTO(song, user);
    }

    public SongDTO unLikeSong(Long id) {
        Song song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song not found"));
        User user = userRepository.findByName(getCurrentUser().getUsername());

        song.getLikedBy().remove(user);
        song = songRepository.save(song);
        return convertSongToSongDTO(song, user);
    }

    public List<SongDataDTO> getLikedSongs() {
        User user = userRepository.findByName(getCurrentUser().getUsername());
        List<Song> songs = songRepository.getLikedSongsByUserId(user.getId());
        return songs.stream().map(song -> convertSongToSongDataDTO(song, user)).collect(Collectors.toList());
    }
}
