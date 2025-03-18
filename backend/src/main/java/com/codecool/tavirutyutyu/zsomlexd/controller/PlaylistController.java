package com.codecool.tavirutyutyu.zsomlexd.controller;

import com.codecool.tavirutyutyu.zsomlexd.model.playlist.NewPlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/all")
    public List<PlaylistDataDTO> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/id/{id}")
    public PlaylistDTO getPlaylistById(@PathVariable long id) {
        return playlistService.getPlaylistById(id);
    }

    @PostMapping("/upload")
    public PlaylistDTO addNewPlaylist(@RequestBody NewPlaylistDTO newPlaylistDTO) {
        return playlistService.addNewPlaylist(newPlaylistDTO);
    }

}
