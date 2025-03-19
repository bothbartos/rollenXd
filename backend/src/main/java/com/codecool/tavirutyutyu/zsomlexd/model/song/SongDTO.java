package com.codecool.tavirutyutyu.zsomlexd.model.song;

public record SongDTO(String title, String audioBase64, String coverBase64, double length,
                      int reShares) {
}
