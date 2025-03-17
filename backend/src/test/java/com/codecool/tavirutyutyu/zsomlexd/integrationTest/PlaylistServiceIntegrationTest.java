package com.codecool.tavirutyutyu.zsomlexd.integrationTest;

import com.codecool.tavirutyutyu.zsomlexd.model.playlist.NewPlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.Playlist;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.PlaylistRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import com.codecool.tavirutyutyu.zsomlexd.service.PlaylistService;
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
class PlaylistServiceIntegrationTest {

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

    @AfterEach
    void tearDown() {
        playlistRepository.deleteAll();
        songRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "TestUser")
    void testAddNewPlaylist() {
        NewPlaylistDTO newPlaylistDTO = new NewPlaylistDTO("Test Playlist", List.of(testSong.getId()));
        PlaylistDTO addedPlaylist = playlistService.addNewPlaylist(newPlaylistDTO);

        assertThat(addedPlaylist).isNotNull();
        assertThat(addedPlaylist.title()).isEqualTo("Test Playlist");
        assertThat(addedPlaylist.songs()).hasSize(1);
        assertThat(addedPlaylist.songs().get(0).title()).isEqualTo("Test Song");
    }

    @Test
    void testGetAllPlaylists() {
        Playlist playlist = new Playlist();
        playlist.setTitle("Test Playlist");
        playlist.setUser(testUser);
        playlist.setSongs(List.of(testSong));
        playlistRepository.save(playlist);

        List<PlaylistDTO> playlists = playlistService.getAllPlaylists();
        assertThat(playlists).isNotEmpty();
        assertThat(playlists.getFirst().title()).isEqualTo("Test Playlist");
    }
}

