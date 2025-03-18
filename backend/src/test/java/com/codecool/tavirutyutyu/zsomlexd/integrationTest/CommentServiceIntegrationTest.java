package com.codecool.tavirutyutyu.zsomlexd.integrationTest;

import com.codecool.tavirutyutyu.zsomlexd.model.comment.Comment;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.CommentDto;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.NewCommentDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.CommentRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import com.codecool.tavirutyutyu.zsomlexd.service.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    private User testUser;
    private Song testSong;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("TestUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        testSong = new Song();
        testSong.setTitle("Test Song");
        testSong.setAuthor(testUser);
        testSong.setAudio(new byte[]{1, 2, 3});
        testSong.setCover(new byte[]{4, 5, 6});
        testSong.setLength(180.0);
        testSong = songRepository.save(testSong);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        songRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testGetCommentsBySongId() {
        Comment comment = new Comment();
        comment.setText("Test Comment");
        comment.setUser(testUser);
        comment.setSong(testSong);
        commentRepository.save(comment);

        List<CommentDto> comments = commentService.getCommentsBySongId(testSong.getId());
        assertThat(comments).isNotEmpty();
        assertThat(comments.getFirst().text()).isEqualTo("Test Comment");
    }

    @Test
    @WithMockUser(username = "TestUser")
    void testAddComment() {
        NewCommentDTO newCommentDTO = new NewCommentDTO(testSong.getId(), "New Test Comment");
        CommentDto addedComment = commentService.addComment(newCommentDTO);

        assertThat(addedComment).isNotNull();
        assertThat(addedComment.text()).isEqualTo("New Test Comment");
        assertThat(addedComment.songId()).isEqualTo(testSong.getId());
    }
}

