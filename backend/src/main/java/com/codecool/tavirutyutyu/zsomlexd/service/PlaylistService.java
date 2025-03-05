package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.model.Playlist;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.PlaylistRepository;
import io.jsonwebtoken.io.Encoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private static final Logger logger = LoggerFactory.getLogger(PlaylistService.class);

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository){
        this.playlistRepository = playlistRepository;
    }

    public List<PlaylistDTO> getAllPlaylists(){
        List<Playlist> playlists = playlistRepository.findAll();
        return playlists.stream().map(this::convertPlaylistToPlaylistDTO).toList();
    }

    private PlaylistDTO convertPlaylistToPlaylistDTO(Playlist playlist){
        List<SongDataDTO> songs = playlist.getSongs()
                .stream()
                .map(song -> new SongDataDTO(song.getTitle(),
                        song.getAuthor().getName(),
                        Base64.getEncoder().encodeToString(song.getCover()),
                        song.getLength(),
                        song.getNumberOfLikes(),
                        song.getReShare(),
                        song.getId()))
                .toList();
        return new PlaylistDTO(playlist.getTitle(), playlist.getUser(), songs);
    }

}
