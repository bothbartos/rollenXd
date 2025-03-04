package com.codecool.tavirutyutyu.zsomlexd.service;


import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.model.user.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.user.UserDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
}
