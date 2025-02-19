package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.model.Comment;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.CommentDto;
import com.codecool.tavirutyutyu.zsomlexd.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDto> getCommentsBySongId(Long songId) {
        List<Comment> comments = commentRepository.findCommentsBySongId(songId);
        return comments.stream().map(this::convertCommentToCommentDto).collect(Collectors.toList());
    }

    private CommentDto convertCommentToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getSong().getId(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                Base64.getEncoder().encodeToString(comment.getUser().getProfile_picture()),
                comment.getText()
        );
    }
}
