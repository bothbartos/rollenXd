package com.codecool.tavirutyutyu.zsomlexd.model.song;

public record SongDataDTO(String title, String author, String coverBase64, double length, int numberOfLikes,
                          int reShares, long id) {
}
