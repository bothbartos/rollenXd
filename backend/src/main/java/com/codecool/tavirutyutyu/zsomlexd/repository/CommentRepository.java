package com.codecool.tavirutyutyu.zsomlexd.repository;

import com.codecool.tavirutyutyu.zsomlexd.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.song.id = :songId")
    List<Comment> findCommentsBySongId(@Param("songId") Long songId);
}