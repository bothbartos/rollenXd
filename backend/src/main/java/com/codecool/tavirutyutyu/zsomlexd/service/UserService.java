package com.codecool.tavirutyutyu.zsomlexd.service;


import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.model.user.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.user.UserDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.codecool.tavirutyutyu.zsomlexd.util.Utils.isImageFile;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        try{
            List<User> users = userRepository.findAll();
            return users.stream().map(this::convertUserToDTO).toList();
        }catch (Exception e){
            throw new RuntimeException("Error getting all users");
        }
    }

    private UserDTO convertUserToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getBio());
    }

    public UserDTO createNewUser(NewUserDTO userDTO) {
        if(userRepository.findByName(userDTO.getName()) != null) {
            throw new IllegalArgumentException("User name already exists");
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        User newUser = userRepository.save(user);
        return convertUserToDTO(newUser);
    }

    public UserDTO addPicture(Long id, MultipartFile profilePicture) throws IOException {
        if(!isImageFile(profilePicture)) {
            throw new IllegalArgumentException("Profile picture is not a valid image file");
        }
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        user.setProfilePicture(profilePicture.getBytes());
        User newUser = userRepository.save(user);
        return convertUserToDTO(newUser);
        }
}
