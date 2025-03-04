package com.codecool.tavirutyutyu.zsomlexd.model;

import java.util.List;

public record JwtResponse(String jwtSecret, String username, List<String> roles) {
}
