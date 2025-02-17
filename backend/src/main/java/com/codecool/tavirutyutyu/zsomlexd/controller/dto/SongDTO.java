package com.codecool.tavirutyutyu.zsomlexd.controller.dto;

public record SongDTO(String title, String audioBase64, String coverBase64, double length, int numberOfLikes,
                      int reShares) {
}
