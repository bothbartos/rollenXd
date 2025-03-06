package com.codecool.tavirutyutyu.zsomlexd.service;

import com.codecool.tavirutyutyu.zsomlexd.model.Playlist;
import com.codecool.tavirutyutyu.zsomlexd.model.Song;
import com.codecool.tavirutyutyu.zsomlexd.model.User;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.NewPlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.model.playlist.PlaylistDTO;
import com.codecool.tavirutyutyu.zsomlexd.repository.PlaylistRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.SongRepository;
import com.codecool.tavirutyutyu.zsomlexd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongRepository songRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PlaylistService playlistService;

    private Playlist playlist;
    private User user;
    private Song song;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("testUser");

        song = new Song();
        song.setId(1L);
        song.setTitle("Test Song");
        song.setAuthor(user);
        song.setCover(new byte[]{1, 2, 3});
        song.setLength(180.0);
        song.setNumberOfLikes(10);
        song.setReShare(5);

        playlist = new Playlist();
        playlist.setTitle("Test Playlist");
        playlist.setUser(user);
        playlist.setSongs(List.of(song));

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


    @Test
    void testGetAllPlaylists() {
        when(playlistRepository.findAll()).thenReturn(List.of(playlist));

        List<PlaylistDTO> playlists = playlistService.getAllPlaylists();

        assertEquals(1, playlists.size());
        assertEquals("Test Playlist", playlists.get(0).title());
        verify(playlistRepository, times(1)).findAll();
    }

    @Test
    void testAddNewPlaylist() {
        NewPlaylistDTO newPlaylistDTO = new NewPlaylistDTO("New Playlist", List.of(1L));
        when(userRepository.findByName(anyString())).thenReturn(user);
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        PlaylistDTO result = playlistService.addNewPlaylist(newPlaylistDTO);

        assertEquals("New Playlist", result.title());
        assertEquals(1, result.songs().size());
        verify(playlistRepository, times(1)).save(any(Playlist.class));
    }

    @Test
    void testAddNewPlaylist_SongNotFound() {
        NewPlaylistDTO newPlaylistDTO = new NewPlaylistDTO("New Playlist", List.of(1L));

        when(userRepository.findByName(anyString())).thenReturn(user);
        when(songRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> playlistService.addNewPlaylist(newPlaylistDTO));
        assertEquals("Song not found", exception.getMessage());
    }
}
