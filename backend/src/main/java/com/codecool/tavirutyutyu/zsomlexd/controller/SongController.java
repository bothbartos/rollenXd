package com.codecool.tavirutyutyu.zsomlexd.controller;

import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.controller.dto.SongUploadDTO;
import com.codecool.tavirutyutyu.zsomlexd.service.SongService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<List<SongDataDTO>> getAllSongs() {
        List<SongDataDTO> allSongs = songService.getAllSongs();
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

    @GetMapping("/search")
    public Set<SongDataDTO> getSongData(@RequestParam String search) {
        return songService.searchSong(search);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadSong(
            @RequestParam("title") String title,
            @RequestParam("length") Integer length,
            @RequestParam("author") String author,
            @RequestPart("file") MultipartFile file) {
        try{
            SongUploadDTO newSongUploadDto = new SongUploadDTO(title, length, author);
            SongDTO createdSong = songService.addSong(newSongUploadDto, file);
            return new ResponseEntity<>(createdSong, HttpStatus.CREATED);
        }catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
