package com.codecool.tavirutyutyu.zsomlexd.model.playlist;

import java.util.List;

public record NewPlaylistDTO(String title, List<Long> songId) {
}
