package com.codecool.tavirutyutyu.zsomlexd.model.user;

import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;

import java.util.List;

public record UserDetailDTO(
        Long id,
        String name,
        String email,
        String bio,
        String profileImageBase64,
        List<SongDataDTO> songs,
        List<PlaylistDataDTO> playlists
) {
}
