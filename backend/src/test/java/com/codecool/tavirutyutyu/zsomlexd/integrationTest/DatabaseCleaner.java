package com.codecool.tavirutyutyu.zsomlexd.integrationTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.codecool.tavirutyutyu.zsomlexd.repository.*;

@Component
public class DatabaseCleaner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PlaylistRepository playlistRepository;


    @Transactional
    public void cleanDatabase() {
        commentRepository.deleteAllInBatch();
        playlistRepository.deleteAllInBatch();
        songRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
}
