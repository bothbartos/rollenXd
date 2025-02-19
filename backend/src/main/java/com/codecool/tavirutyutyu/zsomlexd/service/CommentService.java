package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.Comment;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.CommentDto;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.NewCommentDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.CommentRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, SongService songService, UserRepository userRepository, SongRepository songRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
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

    public CommentDto addComment(NewCommentDTO newComment) {
        Optional<Song> song = songRepository.findById(newComment.songId());
        Optional<User> user = userRepository.findById(newComment.userId());
        if(song.isPresent() && user.isPresent()) {
            Comment comment = new Comment();
            comment.setId(newComment.id());
            comment.setSong(song.get());
            comment.setUser(user.get());
            comment.setText(newComment.text());
            commentRepository.save(comment);
            return convertCommentToCommentDto(comment);
        }
        return null;

    }
}
