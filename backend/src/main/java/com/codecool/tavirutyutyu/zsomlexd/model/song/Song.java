package com.codecool.tavirutyutyu.zsomlexd.model.song;

import com.codecool.tavirutyutyu.zsomlexd.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "song")
@Getter
@Setter
@NoArgsConstructor
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

    @Column(name = "numberoflikes", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer numberOfLikes = 0;

    @Column(name = "re_share", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer reShare = 0;

    public Song(Long id, String title, User author, byte[] cover, Double length, Integer numberOfLikes, Integer reShare) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.cover = cover;
        this.length = length;
        this.numberOfLikes = numberOfLikes;
        this.reShare = reShare;
    }
}
