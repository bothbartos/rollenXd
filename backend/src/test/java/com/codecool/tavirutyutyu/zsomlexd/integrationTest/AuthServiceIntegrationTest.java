package com.codecool.tavirutyutyu.zsomlexd.integrationTest;

import com.codecool.tavirutyutyu.zsomlexd.model.JwtResponse;
import com.codecool.tavirutyutyu.zsomlexd.model.user.LoginRequest;
import com.codecool.tavirutyutyu.zsomlexd.model.user.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        // Clean up users before each test
        userRepository.deleteAll();
    }

    @Test
    void testSignup() throws Exception {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("testSignup");
        newUserDTO.setEmail("testSignup@example.com");
        newUserDTO.setPassword("password");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isCreated());

        // Verify user exists
        assertNotNull(userRepository.findByName("testSignup"));
    }

    @Test
    void testSignup_duplicateUser() throws Exception {
        // Register a user
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("testDuplicate");
        newUserDTO.setEmail("testDuplicate@example.com");
        newUserDTO.setPassword("password");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isCreated());

        // Try registering the same user again
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testLogin() throws Exception {
        // First register a user
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setName("testLogin");
        newUserDTO.setEmail("testLogin@example.com");
        newUserDTO.setPassword("password");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDTO)))
                .andExpect(status().isCreated());

        // Now login the user
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testLogin");
        loginRequest.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Check the response contains a token
        JwtResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), JwtResponse.class);
        assertNotNull(response.jwtSecret());
    }

    @Test
    void testLogin_invalidCredentials() throws Exception {
        // Try logging in without registering
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("invalidUser");
        loginRequest.setPassword("wrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError());
    }
}
