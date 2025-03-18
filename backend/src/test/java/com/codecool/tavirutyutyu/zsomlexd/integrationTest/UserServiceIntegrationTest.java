package com.codecool.tavirutyutyu.zsomlexd.integrationTest;

import com.codecool.tavirutyutyu.zsomlexd.model.user.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.model.user.UserDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import com.codecool.tavirutyutyu.zsomlexd.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateNewUser() {
        NewUserDTO newUser = new NewUserDTO();
        newUser.setName("TestUser");
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");

        UserDTO createdUser = userService.createNewUser(newUser);
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.name()).isEqualTo("TestUser");
        assertThat(createdUser.email()).isEqualTo("test@example.com");

        userRepository.deleteById(createdUser.id());
    }

    @Test
    void testAddPicture() throws IOException {
        User user = new User();
        user.setName("PictureUser");
        user.setEmail("picture@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        MockMultipartFile picture = new MockMultipartFile("picture", "test.jpg", "image/jpeg", "test image content".getBytes());

        UserDTO updatedUser = userService.addPicture(user.getId(), picture);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.id()).isEqualTo(user.getId());
        assertThat(updatedUser.name()).isEqualTo("PictureUser");

        userRepository.deleteById(user.getId());
    }
}
