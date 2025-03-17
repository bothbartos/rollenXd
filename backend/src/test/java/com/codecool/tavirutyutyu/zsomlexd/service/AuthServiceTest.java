package com.codecool.tavirutyutyu.zsomlexd.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.codecool.tavirutyutyu.zsomlexd.model.user.Role;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.model.user.LoginRequest;
import com.codecool.tavirutyutyu.zsomlexd.model.user.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import com.codecool.tavirutyutyu.zsomlexd.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import com.codecool.tavirutyutyu.zsomlexd.model.JwtResponse;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        user = new User();
        user.setName("testUser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRoles(Set.of(Role.ROLE_USER));
    }

    @Test
    void testRegisterUser_Success() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("new@example.com");
        newUserDTO.setPassword("password");
        newUserDTO.setName("testUser");        when(userRepository.findByName(newUserDTO.getName())).thenReturn(null);
        when(userRepository.findByEmail(newUserDTO.getEmail())).thenReturn(Optional.empty());

        authService.registerUser(newUserDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("new@example.com");
        newUserDTO.setPassword("password");
        newUserDTO.setName("testUser");
        when(userRepository.findByName(newUserDTO.getName())).thenReturn(user);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.registerUser(newUserDTO));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username already exists", exception.getReason());
    }

    @Test
    void testRegisterUser_EmailExists() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("test@example.com");
        newUserDTO.setPassword("password123");
        newUserDTO.setName("newUser");
        when(userRepository.findByName(newUserDTO.getName())).thenReturn(null);
        when(userRepository.findByEmail(newUserDTO.getEmail())).thenReturn(Optional.of(user));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.registerUser(newUserDTO));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Email already registered", exception.getReason());
    }

    @Test
    void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password123");
        loginRequest.setUsername("testUser");
        when(userRepository.findByName(loginRequest.getUsername())).thenReturn(user);
        when(jwtUtil.generateJwtToken(user.getName())).thenReturn("mocked-jwt-token");

        JwtResponse response = authService.login(loginRequest);

        assertEquals("mocked-jwt-token", response.jwtSecret());
        assertEquals("testUser", response.username());
        assertTrue(response.roles().contains("ROLE_USER"));
    }

    @Test
    void testLogin_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password123");
        loginRequest.setUsername("testUser");
        when(userRepository.findByName(loginRequest.getUsername())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.login(loginRequest));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Invalid username or password", exception.getReason());
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password123");
        loginRequest.setUsername("testUser");
        when(userRepository.findByName(loginRequest.getUsername())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.login(loginRequest));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Invalid username or password", exception.getReason());
    }
}
