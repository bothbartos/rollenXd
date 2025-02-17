package com.codecool.tavirutyutyu.zsomlexd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "song")
@Getter
@Setter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    @Column(columnDefinition = "BYTEA")
    private byte[] audio;

    private byte[] cover;

    private Double length; // Length in seconds

    @Column(name = "numberoflikes", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer numberOfLikes = 0;

    @Column(name = "re_share", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer reShare = 0;

}
