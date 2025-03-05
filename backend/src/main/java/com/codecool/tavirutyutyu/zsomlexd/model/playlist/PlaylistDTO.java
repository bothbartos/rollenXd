package com.codecool.tavirutyutyu.zsomlexd.model.playlist;

import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;

import java.util.List;

public record PlaylistDTO(String title, User user, List<SongDataDTO> songs) {
}
