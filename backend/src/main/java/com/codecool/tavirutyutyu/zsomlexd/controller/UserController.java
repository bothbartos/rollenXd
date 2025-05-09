package com.codecool.tavirutyutyu.zsomlexd.controller;


import com.codecool.tavirutyutyu.zsomlexd.model.user.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.user.UserDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.user.UserDetailDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.user.UserUpdateDTO;
import com.codecool.tavirutyutyu.zsomlexd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/")
    public UserDTO createUser(@RequestBody NewUserDTO userDTO) {
        return userService.createNewUser(userDTO);
    }

    @PatchMapping("/id/{id}")
    public UserDTO addPicture(
            @PathVariable Long id,
            @RequestPart("profilePicture") MultipartFile profilePicture) throws IOException {
        return userService.addPicture(id, profilePicture);
    }

    @GetMapping("/details")
    public UserDetailDTO getUserDetails() {
        return userService.getUserDetails();
    }

    @PatchMapping("/details/update")
    public UserUpdateDTO updateUserDetails(
            @RequestParam String bio,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {
        if(profilePicture != null) {
            return userService.updateProfile(bio, profilePicture);
        }
        return userService.updateProfile(bio);
    }
}
