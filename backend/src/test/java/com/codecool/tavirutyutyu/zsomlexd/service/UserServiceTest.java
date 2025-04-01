package com.codecool.tavirutyutyu.zsomlexd.service;


import com.codecool.tavirutyutyu.zsomlexd.model.playlist.Playlist;
import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.user.*;
import com.codecool.tavirutyutyu.zsomlexd.repository.PlaylistRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private UserService userService;


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
    void getAllUsers_shouldReturnListOfUserDTOs() {
        // Arrange
        User user1 = new User();
        user1.setPassword("password");
        user1.setEmail("john@example.com");
        user1.setName("John Doe");
        user1.setId(1L);
        user1.setBio("Bio 1");
        user1.setDefaultProfilePicture();

        User user2 = new User();
        user2.setPassword("password");
        user2.setEmail("jane@example.com");
        user2.setName("Jane Doe");
        user2.setId(2L);
        user2.setBio("Bio 2");
        user2.setDefaultProfilePicture();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UserDTO> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).name());
        assertEquals("jane@example.com", result.get(1).email());
    }

    @Test
    void createNewUser_shouldReturnUserDTO() {
        // Arrange
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("John Doe");
        newUserDTO.setEmail("john@example.com");
        newUserDTO.setPassword("password");

        User savedUser = new User();
        savedUser.setPassword("password");
        savedUser.setEmail("john@example.com");
        savedUser.setName("John Doe");
        savedUser.setId(1L);
        savedUser.setBio("Bio 1");
        savedUser.setDefaultProfilePicture();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDTO result = userService.createNewUser(newUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals("john@example.com", result.email());
    }

    @Test
    void addPicture_shouldUpdateUserWithPictureAndReturnUserDTO() throws IOException {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setPassword("password");
        user.setEmail("john@example.com");
        user.setName("John Doe");
        user.setId(1L);
        user.setBio("Bio 1");
        user.setDefaultProfilePicture();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        MultipartFile profilePicture = new MockMultipartFile("profilePicture", "test.jpg", "image/jpeg", "test image content".getBytes());

        // Act
        UserDTO result = userService.addPicture(userId, profilePicture);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.id());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void addPicture_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        MultipartFile profilePicture = new MockMultipartFile("profilePicture", "test.jpg", "image/jpeg", "test image content".getBytes());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.addPicture(userId, profilePicture));
    }

    @Test
    void getUserDetails_shouldReturnUserDetails() {
        //Arrange
        Long userId = 1L;

        User user = new User();
        user.setPassword("password");
        user.setEmail("john@example.com");
        user.setName("testUser");
        user.setId(1L);
        user.setBio("Bio 1");
        user.setDefaultProfilePicture();

        String title1 = "Test Song1";
        Song song = new Song();
        song.setTitle(title1);
        song.setId(1L);
        song.setAudio("audio data".getBytes());
        song.setCover("cover data".getBytes());
        song.setLength(180.0); // 3 minutes
        song.setReShare(50);
        song.setAuthor(user);

        Playlist playlist = new Playlist();
        playlist.setTitle("Test Playlist");
        playlist.setUser(user);
        playlist.setSongs(List.of(song));

        // 2. Mock repository responses
        when(userRepository.findByName(user.getName())).thenReturn(user);
        when(songRepository.findAllWithoutAudioByAuthorName(user.getName())).thenReturn(List.of(song));
        when(playlistRepository.findAllByUserName(user.getName())).thenReturn(List.of(playlist));

        // Act
        UserDetailDTO result = userService.getUserDetails();  // Changed from userId to context-based

        // Assert
        assertNotNull(result);
        assertEquals(user.getName(), result.name());

        // Verify repository calls
        verify(userRepository).findByName(user.getName());
        verify(songRepository).findAllWithoutAudioByAuthorName(user.getName());
        verify(playlistRepository).findAllByUserName(user.getName());
    }

    @Test
    void updateProfile_withBioAndProfilePicture_shouldUpdateUserAndReturnDTO() throws IOException {
        // Arrange
        String bio = "New bio";
        User user = new User();
        user.setName("testUser");
        user.setBio("Old bio");
        user.setProfilePicture("old picture".getBytes());

        MultipartFile profilePicture = new MockMultipartFile(
                "profilePicture",
                "test.jpg",
                "image/jpeg",
                "new picture content".getBytes()
        );

        when(userRepository.findByName("testUser")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserUpdateDTO result = userService.updateProfile(bio, profilePicture);

        // Assert
        assertEquals(bio, user.getBio());
        assertArrayEquals("new picture content".getBytes(), user.getProfilePicture());
        assertNotNull(result);
        assertEquals(bio, result.bio());
        verify(userRepository).findByName("testUser");
        verify(userRepository).save(user);
    }

    @Test
    void updateProfile_withBioOnly_shouldUpdateBioAndReturnDTO() {
        // Arrange
        String bio = "New bio";
        User user = new User();
        user.setName("testUser");
        user.setBio("Old bio");
        user.setProfilePicture("old picture".getBytes());

        when(userRepository.findByName("testUser")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserUpdateDTO result = userService.updateProfile(bio);

        // Assert
        assertEquals(bio, user.getBio());
        assertArrayEquals("old picture".getBytes(), user.getProfilePicture()); // Picture should remain unchanged
        assertNotNull(result);
        assertEquals(bio, result.bio());
        verify(userRepository).findByName("testUser");
        verify(userRepository).save(user);
    }

    @Test
    void updateProfile_withNonImageFile_shouldOnlyUpdateBio() throws IOException {
        // Arrange
        String bio = "New bio";
        User user = new User();
        user.setName("testUser");
        user.setBio("Old bio");
        user.setProfilePicture("old picture".getBytes());

        MultipartFile nonImageFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "text content".getBytes()
        );

        when(userRepository.findByName("testUser")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserUpdateDTO result = userService.updateProfile(bio, nonImageFile);

        // Assert
        assertEquals(bio, user.getBio());
        assertArrayEquals("old picture".getBytes(), user.getProfilePicture()); // Picture should remain unchanged
        verify(userRepository).findByName("testUser");
        verify(userRepository).save(user);
    }


}
