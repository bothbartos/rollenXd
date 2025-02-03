package com.codecool.tavirutyutyu.zsomlexd.controller;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/song")
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }


    @GetMapping("/id/{id}")
    public ResponseEntity<byte[]> getAudioFile(@PathVariable Long id) {
        byte[] audioData = songService.getAudioById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(audioData.length);
        return new ResponseEntity<>(audioData, headers, HttpStatus.OK);
    }
    @GetMapping("/title/{title}")
    public ResponseEntity<SongDTO> getAudioFile(@PathVariable String title) {
        SongDTO songDTO = songService.getAudioByTitle(title);
        return ResponseEntity.ok(songDTO);
    }
    @GetMapping("/all")
    public ResponseEntity<List<SongDTO>> getAllSongs() {
        List<SongDTO> allSongs = songService.getAllSongs();
        return ResponseEntity.ok(allSongs);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable Long id) {
        try{
            songService.deleteSongById(id);
            return new ResponseEntity<>("Song deleted", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
