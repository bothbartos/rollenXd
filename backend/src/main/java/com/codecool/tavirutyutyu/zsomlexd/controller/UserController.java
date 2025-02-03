package com.codecool.tavirutyutyu.zsomlexd.controller;


import com.codecool.tavirutyutyu.zsomlexd.controller.dto.NewUserDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.UserDTO;
import com.codecool.tavirutyutyu.zsomlexd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
