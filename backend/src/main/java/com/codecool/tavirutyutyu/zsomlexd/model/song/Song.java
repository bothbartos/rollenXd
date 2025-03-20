package com.codecool.tavirutyutyu.zsomlexd.model.song;

import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "song")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    @Column(columnDefinition = "BYTEA")
    private byte[] audio;

    @Column(columnDefinition = "BYTEA")
    private byte[] cover;

    private Double length; // Length in seconds

    @ManyToMany
    @JoinTable(
            name = "song_likes",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedBy = new HashSet<>();

    @Column(name = "re_share", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer reShare = 0;

    public Song(Long id, String title, User author, byte[] cover, Double length, Integer reShare) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.length = length;
        this.reShare = reShare;
    }
}
