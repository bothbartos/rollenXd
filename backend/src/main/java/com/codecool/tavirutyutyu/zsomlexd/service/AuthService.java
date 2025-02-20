package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.config.JwtUtil;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.LoginRequest;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;


    private final JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @Autowired
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }

    /**
     * Register a new user and return a JWT token
     */
    public String registerUser(NewUserDTO newUserDTO) {
        if (userRepository.findByName(newUserDTO.getName()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        if (userRepository.findByEmail(newUserDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = new User();
        user.setName(newUserDTO.getName());
        user.setEmail(newUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        user.setDefaultProfilePicture();

        userRepository.save(user);

        return jwtUtil.generateToken(user.getName());
    }

    /**
     * Login user and return a JWT token
     */
    public String login(LoginRequest loginRequest) {
        logger.info("Login request: {}", loginRequest);

        User user = userRepository.findByName(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        return jwtUtil.generateToken(user.getName()); // Uses username for JWT
    }
}
