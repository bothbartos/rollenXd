package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.Comment;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.CommentDto;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.NewCommentDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.CommentRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codecool.tavirutyutyu.zsomlexd.util.Utils.getCurrentUsername;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, SongRepository songRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    public List<CommentDto> getCommentsBySongId(Long songId) {
        try{
            List<Comment> comments = commentRepository.findCommentsBySongId(songId);
            if(comments.isEmpty()) return new ArrayList<>();
            return comments.stream().map(this::convertCommentToCommentDto).collect(Collectors.toList());
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("Comments not found");
        }
    }

    private CommentDto convertCommentToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getSong().getId(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                Base64.getEncoder().encodeToString(comment.getUser().getProfilePicture()),
                comment.getText()
        );
    }

    public CommentDto addComment(NewCommentDTO newComment) {
        Optional<Song> song = songRepository.findById(newComment.songId());
        User user = userRepository.findByName(getCurrentUsername().getUsername());
        if(song.isPresent()) {
            Comment comment = new Comment();
            comment.setSong(song.get());
            comment.setUser(user);
            comment.setText(newComment.text());
            commentRepository.save(comment);
            return convertCommentToCommentDto(comment);
        }
        throw new EntityNotFoundException("Song not found");
    }

}
