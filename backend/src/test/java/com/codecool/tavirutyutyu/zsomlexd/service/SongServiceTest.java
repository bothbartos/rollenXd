package com.codecool.tavirutyutyu.zsomlexd.service;


import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongUploadDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SongService songService;


    @BeforeEach
    void setUp() {
        setUpSecurityContext();
    }

    void setUpSecurityContext() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.lenient().when(userDetails.getUsername()).thenReturn("testUser");
        Mockito.lenient().when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.lenient().when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllSongs_shouldReturnListOfSongDataDTOs() {
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

        song1.setReShare(50);
        song1.setAuthor(user);

        String title2 = "Test Song2";
        Song song2 = new Song();
        song2.setId(2L);
        song2.setTitle(title2);
        song2.setAudio("audio data".getBytes());
        song2.setCover("cover data".getBytes());
        song2.setLength(180.0); // 3 minutes

        song2.setReShare(50);
        song2.setAuthor(user);

        when(songRepository.findAllWithoutAudio()).thenReturn(Arrays.asList(song1, song2));

        List<SongDataDTO> result = songService.getAllSongs();

        assertEquals(2, result.size());
    }

    @Test
    void deleteSongById_shouldDeleteExistingSong() {
        Long id = 1L;
        when(songRepository.findById(id)).thenReturn(Optional.of(new Song()));

        assertDoesNotThrow(() -> songService.deleteSongById(id));

        verify(songRepository).deleteById(id);
    }

    @Test
    void deleteSongById_shouldThrowExceptionWhenSongNotFound() {
        Long id = 1L;
        when(songRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            songService.deleteSongById(id);
        });

        assertEquals("Song not found with id: " + id, thrown.getMessage());
    }

    @Test
    void searchSong_shouldReturnSetOfSongs() {
        String searchString = "Test";

        User user = new User();
        user.setName("testUser");
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
        song1.setLikedBy(new HashSet<>());

        song1.setReShare(50);


        when(songRepository.findDistinctByTitleOrAuthorContainingIgnoreCase(searchString))
                .thenReturn(List.of(song1));

        var result = songService.searchSong(searchString);

        assertEquals(1, result.size());
    }
    @Test
    void addSong_shouldReturnSongDTO() {
        SongUploadDTO uploadDTO = new SongUploadDTO("New Song", "Author Name");
        MockMultipartFile audioFile = new MockMultipartFile("file", "audio.mp3", "audio/mpeg", "audio data".getBytes());
        MockMultipartFile coverFile = new MockMultipartFile("cover", "cover.jpg", "image/jpeg", "cover image".getBytes());

        User user = new User();
        user.setName("Author Name");


        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "Author Name", "", Collections.emptyList()
        );

        SecurityContextHolder.setContext(new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken(mockUserDetails, null, mockUserDetails.getAuthorities())
        ));

        when(userRepository.findByName(uploadDTO.author())).thenReturn(user);
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SongDTO result = songService.addSong(uploadDTO.title(), audioFile, coverFile);

        SecurityContextHolder.clearContext();

        assertNotNull(result);
        assertEquals("New Song", result.title());
    }

    @Test
    void addSong_shouldThrowExceptionWhenAudioFileIsEmpty() {
        SongUploadDTO uploadDTO = new SongUploadDTO("New Song", "Author Name");
        MockMultipartFile emptyAudioFile = new MockMultipartFile("file", "audio.mp3", "audio/mpeg", new byte[0]);

        MockMultipartFile coverFile = new MockMultipartFile("cover", "cover.jpg", "image/jpeg", "cover image".getBytes());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            songService.addSong(uploadDTO.title(), emptyAudioFile, coverFile);
        });

        assertEquals("Audio or cover file cannot be empty", thrown.getMessage());
    }

    @Test
    void getAllSongs_shouldThrowRuntimeExceptionWhenRepositoryFails() {
        when(songRepository.findAllWithoutAudio()).thenThrow(new RuntimeException("Songs not found"));

        assertThrows(RuntimeException.class, () -> songService.getAllSongs());
    }

    @Test
    void searchSong_shouldThrowRuntimeExceptionWhenRepositoryFails() {
        when(songRepository.findDistinctByTitleOrAuthorContainingIgnoreCase(anyString()))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> songService.searchSong("test"));
    }

    @Test
    void addSong_shouldThrowIllegalArgumentExceptionWhenCoverFileIsNotImage() throws IOException {
        SongUploadDTO uploadDTO = new SongUploadDTO("New Song", "Author Name");
        MockMultipartFile audioFile = new MockMultipartFile("file", "audio.mp3", "audio/mpeg", "audio data".getBytes());
        MockMultipartFile coverFile = new MockMultipartFile("cover", "cover.txt", "text/plain", "not an image".getBytes());

        assertThrows(IllegalArgumentException.class, () -> songService.addSong(uploadDTO.title(), audioFile, coverFile));
    }

    @Test
    void addSong_shouldThrowIllegalArgumentExceptionWhenAudioFileIsNotMp3() throws IOException {
        SongUploadDTO uploadDTO = new SongUploadDTO("New Song", "Author Name");
        MockMultipartFile audioFile = new MockMultipartFile("file", "audio.wav", "audio/wav", "audio data".getBytes());
        MockMultipartFile coverFile = new MockMultipartFile("cover", "cover.jpg", "image/jpeg", "cover image".getBytes());

        assertThrows(IllegalArgumentException.class, () -> songService.addSong(uploadDTO.title(), audioFile, coverFile));
    }

    @Test
    void getAudioStreamById_shouldReturnInputStreamWhenSongExists() throws IOException {
        Long id = 1L;
        Song song = new Song();
        song.setAudio("audio data".getBytes());
        when(songRepository.findById(id)).thenReturn(Optional.of(song));

        InputStream result = songService.getAudioStreamById(id);

        assertNotNull(result);
        assertEquals("audio data", new String(result.readAllBytes()));
    }

    @Test
    void getAudioStreamById_shouldThrowEntityNotFoundExceptionWhenSongNotFound() {
        Long id = 1L;
        when(songRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> songService.getAudioStreamById(id));
    }

    @Test
    void getSongDetailsById_shouldReturnSongDataDTOWhenSongExists() {
        Long id = 1L;
        Song song = new Song();
        song.setId(id);
        song.setTitle("Test Song");

        User user = new User();
        user.setName("Test Author");

        song.setAuthor(user);
        song.setCover("cover data".getBytes());
        song.setLength(180.0);
        song.setReShare(50);
        song.setLikedBy(new HashSet<>());

        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                user.getName(), "", Collections.emptyList()
        );

        SecurityContextHolder.setContext(new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken(mockUserDetails, null, mockUserDetails.getAuthorities())
        ));


        when(songRepository.findById(id)).thenReturn(Optional.of(song));

        SongDataDTO result = songService.getSongDetailsById(id);

        assertNotNull(result);
        assertEquals("Test Song", result.title());
        assertEquals("Test Author", result.author());
    }

    @Test
    void getSongDetailsById_shouldThrowEntityNotFoundExceptionWhenSongNotFound() {
        Long id = 1L;
        when(songRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> songService.getSongDetailsById(id));
    }

    @Test
    void likeSong_ShouldAddUserToLikedByAndReturnSongDTO() {
        // Arrange
        Long songId = 1L;
        User user = new User();
        user.setId(1L);
        user.setName("testUser");

        String title = "Test Song";
        Song song = new Song();
        song.setId(1L);
        song.setAuthor(user);
        song.setTitle(title);
        song.setAudio("audio data".getBytes());
        song.setCover("cover data".getBytes());
        song.setLength(180.0);
        song.setLikedBy(new HashSet<>());

        song.setReShare(50);


        when(userRepository.findByName("testUser")).thenReturn(user);
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SongDTO result = songService.likeSong(songId);

        // Assert
        assertTrue(song.getLikedBy().contains(user));
        verify(songRepository).save(song);
        verify(songRepository).findById(songId);
        verify(userRepository).findByName("testUser");

        // Verify the returned DTO has expected values
        assertTrue(result.isLiked());
        assertEquals(song.getTitle(), result.title());
    }

    @Test
    void unLikeSong_ShouldRemoveUserFromLikedByAndReturnSongDTO() {
        // Arrange
        Long songId = 1L;
        User user = new User();
        user.setId(1L);
        user.setName("testUser");

        String title = "Test Song";
        Song song = new Song();
        song.setId(1L);
        song.setAuthor(user);
        song.setTitle(title);
        song.setAudio("audio data".getBytes());
        song.setCover("cover data".getBytes());
        song.setLength(180.0);
        song.setLikedBy(new HashSet<>());

        song.setReShare(50);


        Set<User> likedBy = new HashSet<>();
        likedBy.add(user);
        song.setLikedBy(likedBy);

        when(userRepository.findByName("testUser")).thenReturn(user);
        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SongDTO result = songService.unLikeSong(songId);

        // Assert
        assertFalse(song.getLikedBy().contains(user));
        verify(songRepository).save(song);
        verify(songRepository).findById(songId);
        verify(userRepository).findByName("testUser");

        assertFalse(result.isLiked());
        assertEquals(song.getTitle(), result.title());
    }

    @Test
    void getLikedSongs_ShouldReturnListOfLikedSongs() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("testUser");

        String title = "Test Song";
        Song song1 = new Song();
        song1.setId(1L);
        song1.setAuthor(user);
        song1.setTitle(title);
        song1.setAudio("audio data".getBytes());
        song1.setCover("cover data".getBytes());
        song1.setLength(180.0);
        song1.setLikedBy(new HashSet<>());
        song1.setReShare(50);

        String title2 = "Test Song2";
        Song song2 = new Song();
        song2.setId(2L);
        song2.setTitle(title2);
        song2.setAudio("audio data".getBytes());
        song2.setCover("cover data".getBytes());
        song2.setLength(180.0); // 3 minutes
        song2.setReShare(50);
        song2.setAuthor(user);

        List<Song> likedSongs = Arrays.asList(song1, song2);

        when(userRepository.findByName("testUser")).thenReturn(user);
        when(songRepository.getLikedSongsByUserId(user.getId())).thenReturn(likedSongs);

        // Act
        List<SongDataDTO> result = songService.getLikedSongs();

        // Assert
        assertEquals(2, result.size());
        verify(songRepository).getLikedSongsByUserId(user.getId());
        verify(userRepository).findByName("testUser");

        // Verify the returned DTOs have expected values
        assertEquals(song1.getId(), result.get(0).id());
        assertEquals(song1.getTitle(), result.get(0).title());
        assertEquals(song2.getId(), result.get(1).id());
        assertEquals(song2.getTitle(), result.get(1).title());
    }

    @Test
    void likeSong_SongNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long songId = 1L;
        when(songRepository.findById(songId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> songService.likeSong(songId));
        verify(songRepository).findById(songId);
        verify(songRepository, never()).save(any(Song.class));
    }

    @Test
    void unLikeSong_SongNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long songId = 1L;
        when(songRepository.findById(songId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> songService.unLikeSong(songId));
        verify(songRepository).findById(songId);
        verify(songRepository, never()).save(any(Song.class));
    }


}
