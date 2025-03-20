package com.codecool.tavirutyutyu.zsomlexd.util;

import com.codecool.tavirutyutyu.zsomlexd.model.playlist.Playlist;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

public class Utils {



    public static boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public static boolean isAudioFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("audio/mpeg") || contentType.equals("audio/mp3"));
    }

    public static UserDetails getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return (UserDetails) principal;
        }else{
            throw new EntityNotFoundException("User not found");
        }
    }

    public static SongDataDTO convertSongToSongDataDTO(Song song) {
        String coverBase64 = Base64.getEncoder().encodeToString(song.getCover());
        return new SongDataDTO(song.getTitle(), song.getAuthor().getName(),coverBase64 ,song.getLength(), song.getNumberOfLikes(), song.getReShare(), song.getId());
    }

    public static PlaylistDataDTO convertPlaylistToPlaylistDataDTO(Playlist playlist){
        return new PlaylistDataDTO(
                playlist.getId(), playlist.getTitle(), playlist.getUser().getName()
        );
    }

}
