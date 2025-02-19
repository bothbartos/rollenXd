package com.codecool.tavirutyutyu.zsomlexd.controller;


import com.codecool.tavirutyutyu.zsomlexd.model.comment.CommentDto;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.NewCommentDTO;
import com.codecool.tavirutyutyu.zsomlexd.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("id/{songId}")
    public ResponseEntity<List<CommentDto>> getCommentsBySongId(@PathVariable("songId") Long songId) {
        return new ResponseEntity<>(commentService.getCommentsBySongId(songId), HttpStatus.OK);
    }

    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(
            @RequestParam("songId") Long songId,
            @RequestParam("userId") Long userId,
            @RequestParam("id") Long id,
            @RequestParam("text") String text
            ){
        NewCommentDTO newComment = new NewCommentDTO(songId, userId, id, text);
        CommentDto createdComment = commentService.addComment(newComment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}
