package com.codecool.tavirutyutyu.zsomlexd.controller;


import com.codecool.tavirutyutyu.zsomlexd.model.comment.CommentDto;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.NewCommentDTO;
import com.codecool.tavirutyutyu.zsomlexd.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("id/{songId}")
    public ResponseEntity<List<CommentDto>> getCommentsBySongId(@PathVariable("songId") Long songId) {
        return new ResponseEntity<>(commentService.getCommentsBySongId(songId), HttpStatus.OK);
    }

    @PostMapping(path = "/addComment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addComment(
            @RequestParam("songId") Long songId,
            @RequestParam("user") String user,
            @RequestParam("text") String text
            ){
        logger.info("Adding comment for user {} with text {}", user, text);
        NewCommentDTO newComment = new NewCommentDTO(user, songId, text);
        CommentDto createdComment = commentService.addComment(newComment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}
