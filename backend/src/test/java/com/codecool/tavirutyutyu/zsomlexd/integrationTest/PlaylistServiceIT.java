package com.codecool.tavirutyutyu.zsomlexd.integrationTest;

import com.codecool.tavirutyutyu.zsomlexd.model.playlist.NewPlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.Playlist;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.user.Role;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.PlaylistRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import com.codecool.tavirutyutyu.zsomlexd.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaylistServiceIT extends ITBase {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistRepository playlistRepository;

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

    @Test
    @WithMockUser(username = "TestUser")
    @Transactional
    void testAddNewPlaylist() {
        User user = new User();
        user.setName("Liker");
        user.setEmail("liker@example.com");
        user.setPassword("password");
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        userRepository.save(user);

        Song song = new Song();
        song.setTitle("Test Song");
        song.setAuthor(testUser);
        song.setAudio(new byte[]{1, 2, 3});
        song.setLength(180.0);
        song.setLikedBy(new HashSet<>());
        NewPlaylistDTO newPlaylistDTO = new NewPlaylistDTO("Test Playlist", List.of(testSong.getId()));

        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                user.getName(), "", Collections.emptyList()
        );

        SecurityContextHolder.setContext(new SecurityContextImpl(
                new UsernamePasswordAuthenticationToken(mockUserDetails, null, mockUserDetails.getAuthorities())
        ));

        PlaylistDTO addedPlaylist = playlistService.addNewPlaylist(newPlaylistDTO);
        assertThat(addedPlaylist).isNotNull();
        assertThat(addedPlaylist.title()).isEqualTo("Test Playlist");
        assertThat(addedPlaylist.songs()).hasSize(1);
        assertThat(addedPlaylist.songs().getFirst().title()).isEqualTo("Test Song");
    }

    @Test
    void testGetAllPlaylists() {
        Playlist playlist = new Playlist();
        playlist.setTitle("Test Playlist");
        playlist.setUser(testUser);
        playlist.setSongs(List.of(testSong));
        playlistRepository.save(playlist);

        List<PlaylistDataDTO> playlists = playlistService.getAllPlaylists();
        assertThat(playlists).isNotEmpty();
        assertThat(playlists.getFirst().title()).isEqualTo("Test Playlist");
    }
}
