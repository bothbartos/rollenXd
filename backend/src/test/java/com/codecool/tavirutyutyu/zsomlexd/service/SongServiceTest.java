package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongUploadDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SongService songService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAudioByTitle_shouldReturnSongDTO() {
        // Arrange
        String title = "Test Song";
        Song song = new Song();
        song.setTitle(title);
        song.setAudio("audio data".getBytes());
        song.setCover("cover data".getBytes());
        song.setLength(180.0); // 3 minutes
        song.setNumberOfLikes(100);
        song.setReShare(50);


        when(songRepository.findByTitle(title)).thenReturn(Optional.of(song));

        // Act
        SongDTO result = songService.getAudioByTitle(title);

        // Assert
        assertNotNull(result);
        assertEquals(title, result.title());
    }

    @Test
    void getAudioByTitle_shouldThrowExceptionWhenSongNotFound() {
        // Arrange
        String title = "Nonexistent Song";
        when(songRepository.findByTitle(title)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            songService.getAudioByTitle(title);
        });
        assertEquals("Song not found", thrown.getMessage());
    }

    @Test
    void getAllSongs_shouldReturnListOfSongDataDTOs() {
        // Arrange
        User user = new User();
        user.setName("Test user");
        user.setPassword("password");
        user.setEmail("test@test.com");
        user.setProfilePicture("picture".getBytes());
        user.setBio("test bio");


        String title1 = "Test Song1";
        Song song1 = new Song();
        song1.setTitle(title1);
        song1.setId(1L);
        song1.setAudio("audio data".getBytes());
        song1.setCover("cover data".getBytes());
        song1.setLength(180.0); // 3 minutes
        song1.setNumberOfLikes(100);
        song1.setReShare(50);
        song1.setAuthor(user);

        String title2 = "Test Song2";
        Song song2 = new Song();
        song2.setId(2L);
        song2.setTitle(title2);
        song2.setAudio("audio data".getBytes());
        song2.setCover("cover data".getBytes());
        song2.setLength(180.0); // 3 minutes
        song2.setNumberOfLikes(100);
        song2.setReShare(50);
        song2.setAuthor(user);


        when(songRepository.findAll()).thenReturn(Arrays.asList(song1, song2));

        // Act
        List<SongDataDTO> result = songService.getAllSongs();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void deleteSongById_shouldDeleteExistingSong() {
        // Arrange
        Long id = 1L;
        when(songRepository.findById(id)).thenReturn(Optional.of(new Song()));

        // Act
        assertDoesNotThrow(() -> songService.deleteSongById(id));

        // Assert
        verify(songRepository).deleteById(id);
    }

    @Test
    void deleteSongById_shouldThrowExceptionWhenSongNotFound() {
        // Arrange
        Long id = 1L;
        when(songRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            songService.deleteSongById(id);
        });

        assertEquals("Song not found with id: " + id, thrown.getMessage());
    }

    @Test
    void searchSong_shouldReturnSetOfSongs() {
        // Arrange
        String searchString = "Test";

        User user = new User();
        user.setName("Test user");
        user.setPassword("password");
        user.setEmail("test@test.com");
        user.setProfilePicture("picture".getBytes());
        user.setBio("test bio");

        String title = "Test Song";
        Song song1 = new Song();
        song1.setId(1L);
        song1.setAuthor(user);
        song1.setTitle(title);
        song1.setAudio("audio data".getBytes());
        song1.setCover("cover data".getBytes());
        song1.setLength(180.0);
        song1.setNumberOfLikes(100);
        song1.setReShare(50);

        when(songRepository.findDistinctByTitleOrAuthorContainingIgnoreCase(searchString))
                .thenReturn(Arrays.asList(song1));

        // Act
        var result = songService.searchSong(searchString);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void addSong_shouldReturnSongDTO() throws IOException {
        // Arrange
        SongUploadDTO uploadDTO = new SongUploadDTO("New Song", "Author Name");
        MockMultipartFile audioFile = new MockMultipartFile("file", "audio.mp3", "audio/mpeg", "audio data".getBytes());
        MockMultipartFile coverFile = new MockMultipartFile("cover", "cover.jpg", "image/jpeg", "cover image".getBytes());

        User user = new User();
        user.setName("Author Name");

        when(userRepository.findByName(uploadDTO.author())).thenReturn(user);
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SongDTO result = songService.addSong(uploadDTO, audioFile, coverFile);

        // Assert
        assertNotNull(result);
        assertEquals("New Song", result.title());
    }

    @Test
    void addSong_shouldThrowExceptionWhenAudioFileIsEmpty() {
        // Arrange
        SongUploadDTO uploadDTO = new SongUploadDTO("New Song", "Author Name");
        MockMultipartFile emptyAudioFile = new MockMultipartFile("file", "audio.mp3", "audio/mpeg", new byte[0]);

        MockMultipartFile coverFile = new MockMultipartFile("cover", "cover.jpg", "image/jpeg", "cover image".getBytes());

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            songService.addSong(uploadDTO, emptyAudioFile, coverFile);
        });

        assertEquals("Audio file cannot be empty", thrown.getMessage());
    }
}
