package com.codecool.tavirutyutyu.zsomlexd.model.playlist;

import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;

import java.util.List;

public record PlaylistDTO(String title, String author, List<SongDataDTO> songs) {
}
