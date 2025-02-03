package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.UserDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertUserToDTO).toList();
    }

    private UserDTO convertUserToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getBio());
    }

    public UserDTO createNewUser(NewUserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        User newUser = userRepository.save(user);
        return convertUserToDTO(newUser);
    }
}
