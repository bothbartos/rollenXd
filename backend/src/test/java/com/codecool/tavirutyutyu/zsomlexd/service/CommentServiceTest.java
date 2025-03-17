package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.Comment;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.CommentDto;
import com.codecool.tavirutyutyu.zsomlexd.model.comment.NewCommentDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.CommentRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private CommentService commentService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        setUpSecurityContext();
    }
    void setUpSecurityContext() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.lenient().when(userDetails.getUsername()).thenReturn("testUser");
        Mockito.lenient().when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.lenient().when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getCommentsBySongId_shouldReturnListOfCommentDtos() {
        // Arrange
        Long songId = 1L;
        Song song = new Song();
        song.setId(songId);

        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setProfilePicture("profile picture".getBytes());

        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setSong(song);
        comment1.setUser(user);
        comment1.setText("Test comment 1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setSong(song);
        comment2.setUser(user);
        comment2.setText("Test comment 2");

        when(commentRepository.findCommentsBySongId(songId)).thenReturn(Arrays.asList(comment1, comment2));

        // Act
        List<CommentDto> result = commentService.getCommentsBySongId(songId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test comment 1", result.get(0).text());
        assertEquals("Test comment 2", result.get(1).text());
    }



    @Test
    void addComment_shouldReturnCommentDto() {
        // Arrange
        Long songId = 1L;
        String userName = "testUser";
        String commentText = "Test comment";

        Song song = new Song();
        song.setId(songId);

        User user = new User();
        user.setId(1L);
        user.setName(userName);
        user.setProfilePicture("profile picture".getBytes());

        NewCommentDTO newComment = new NewCommentDTO(songId, commentText);

        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(userRepository.findByName(userName)).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setId(1L);
            return savedComment;
        });

        // Act
        CommentDto result = commentService.addComment(newComment);

        // Assert
        assertNotNull(result);
        assertEquals(songId, result.songId());
        assertEquals(user.getId(), result.userId());
        assertEquals(userName, result.username());
        assertEquals(commentText, result.text());
        assertNotNull(result.profilePicture());
    }

    @Test
    void addComment_shouldThrowExceptionWhenSongNotFound() {
        // Arrange
        User user = new User();
        user.setName("Test user");
        user.setPassword("password");
        user.setEmail("test@test.com");
        user.setProfilePicture("picture".getBytes());
        user.setBio("test bio");

        Long songId = 1L;
        String commentText = "Test comment";

        NewCommentDTO newComment = new NewCommentDTO(songId, commentText);

        when(songRepository.findById(songId)).thenReturn(Optional.empty());


        // Assert
        assertThrows(EntityNotFoundException.class, () -> commentService.addComment(newComment));
    }
}
