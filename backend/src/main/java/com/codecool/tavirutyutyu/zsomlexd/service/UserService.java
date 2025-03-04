package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.UserDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.UserEntity;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(this::convertUserToDTO).toList();
    }

    private UserDTO convertUserToDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getPassword(), userEntity.getBio());
    }

    public UserDTO createNewUser(NewUserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());
        UserEntity newUserEntity = userRepository.save(userEntity);
        return convertUserToDTO(newUserEntity);
    }

    public UserDTO addPicture(Long id, MultipartFile profilePicture) throws IOException {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        userEntity.setProfilePicture(profilePicture.getBytes());
        UserEntity newUserEntity = userRepository.save(userEntity);
        return convertUserToDTO(newUserEntity);
        }
}
