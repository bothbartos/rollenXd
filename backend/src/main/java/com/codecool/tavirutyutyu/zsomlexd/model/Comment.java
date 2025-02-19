package com.codecool.tavirutyutyu.zsomlexd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {

    @Id
    private long id;

    @ManyToOne(optional = false)
    private Song song;

    @ManyToOne(optional = false)
    private User user;

    private String text;
}
