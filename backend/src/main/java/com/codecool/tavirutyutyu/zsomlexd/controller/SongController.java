package com.codecool.tavirutyutyu.zsomlexd.controller;

import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongUploadDTO;
import com.codecool.tavirutyutyu.zsomlexd.service.SongService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/song")
public class SongController {

    private final SongService songService;
    private final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping(value = "/id/{id}")
    public SongDataDTO getSongDataById(@PathVariable("id") Long id) {
        return songService.getSongDetailsById(id);
    }


    @GetMapping(value = "/stream/{id}", produces = "audio/mpeg")
    public ResponseEntity<Resource> streamAudio(
            @PathVariable Long id,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {

        logger.info("Streaming audio for ID: {}", id);

        InputStream audioStream = songService.getAudioStreamById(id);
        if (audioStream == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        long fileSize = audioStream.available();
        long startByte = 0;
        long endByte = fileSize - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            try {
                startByte = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    endByte = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        long contentLength = endByte - startByte + 1;
        InputStreamResource inputStreamResource = new InputStreamResource(audioStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        headers.setContentLength(contentLength);
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + startByte + "-" + endByte + "/" + fileSize);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers).body(inputStreamResource);
    }


    @GetMapping("/all")
    public List<SongDataDTO> getAllSongs() {
        return songService.getAllSongs();
    }

    @DeleteMapping("/delete/id/{id}")
    public String deleteSong(@PathVariable Long id) {
        songService.deleteSongById(id);
        return "Song deleted";
    }

    @GetMapping("/search")
    public Set<SongDataDTO> getSongData(@RequestParam String search) {
        return songService.searchSong(search);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SongDTO uploadSong(
            @RequestParam("title") String title,
            @RequestPart("file") MultipartFile file,
            @RequestPart("cover") MultipartFile cover) {
            return songService.addSong(title, file, cover);
    }

    @PostMapping("/like/id/{id}")
    public SongDTO likeSong(@PathVariable Long id) {
        return songService.likeSong(id);
    }

    @DeleteMapping("/unlike/id/{id}")
    public SongDTO unLikeSong(@PathVariable Long id) {
        return songService.unLikeSong(id);
    }
}
