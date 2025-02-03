package com.codecool.tavirutyutyu.zsomlexd.controller;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.LoginRequest;
import com.codecool.tavirutyutyu.zsomlexd.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        String response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
