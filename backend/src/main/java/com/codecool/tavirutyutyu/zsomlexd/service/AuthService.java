package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.model.JwtResponse;
import com.codecool.tavirutyutyu.zsomlexd.model.Role;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.security.jwt.JwtUtil;
import com.codecool.tavirutyutyu.zsomlexd.model.user.LoginRequest;
import com.codecool.tavirutyutyu.zsomlexd.model.user.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

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
    public void registerUser(NewUserDTO newUserDTO) {
        if (userRepository.findByName(newUserDTO.getName()) != null) {
            logger.info("User with name {} already exists", newUserDTO.getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        if (userRepository.findByEmail(newUserDTO.getEmail()).isPresent()) {
            logger.info("User with email {} already exists", newUserDTO.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        logger.info("Registering user: {}", newUserDTO.getName());


        User user = new User();
        user.setName(newUserDTO.getName());
        user.setEmail(newUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        user.setDefaultProfilePicture();
        user.setRoles(Set.of(Role.ROLE_USER));

        userRepository.save(user);
    }

    /**
     * Login user and return a JWT token
     */
    public JwtResponse login(LoginRequest loginRequest) {
        logger.info("Login request: {}", loginRequest);
        User user = userRepository.findByName(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return new JwtResponse(jwtUtil.generateJwtToken(user.getName()), user.getName(), user.getRoles().stream().map(Enum::toString).toList());
    }
}
