package com.codecool.tavirutyutyu.zsomlexd.integrationTest;

import com.codecool.tavirutyutyu.zsomlexd.model.song.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.song.SongDataDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import com.codecool.tavirutyutyu.zsomlexd.service.SongService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SongServiceIntegrationTest {

    @Autowired
    private SongService songService;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("TestUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        songRepository.deleteAll();
        userRepository.delete(testUser);
    }

    @Test
    void testGetAllSongs() {
        Song song = new Song();
        song.setTitle("Test Song");
        song.setAuthor(testUser);
        song.setAudio(new byte[]{1, 2, 3});
        song.setCover(new byte[]{4, 5, 6});
        song.setLength(180.0);
        songRepository.save(song);

        List<SongDataDTO> songs = songService.getAllSongs();
        assertThat(songs).isNotEmpty();
        assertThat(songs.getFirst().title()).isEqualTo("Test Song");
    }

    @Test
    @WithMockUser(username = "TestUser")
    void testAddSong() {
        String title = "New Test Song";
        MockMultipartFile audioFile = new MockMultipartFile("audio", "test.mp3", "audio/mpeg", "test audio content".getBytes());
        MockMultipartFile coverFile = new MockMultipartFile("cover", "cover.jpg", "image/jpeg", "test cover content".getBytes());

        SongDTO addedSong = songService.addSong(title, audioFile, coverFile);

        assertThat(addedSong).isNotNull();
        assertThat(addedSong.title()).isEqualTo(title);
    }

    @Test
    void testSearchSong() {
        Song song = new Song();
        song.setTitle("Searchable Song");
        song.setAuthor(testUser);
        song.setAudio(new byte[]{1, 2, 3});
        song.setCover(new byte[]{4, 5, 6});
        song.setLength(180.0);
        songRepository.save(song);

        Set<SongDataDTO> searchResults = songService.searchSong("Searchable");
        assertThat(searchResults).isNotEmpty();
        assertThat(searchResults.iterator().next().title()).isEqualTo("Searchable Song");
    }
}
