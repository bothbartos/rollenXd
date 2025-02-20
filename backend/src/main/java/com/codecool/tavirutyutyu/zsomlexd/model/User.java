package com.codecool.tavirutyutyu.zsomlexd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Entity
@Table(name = "user_table")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "BYTEA")
    private byte[] profile_picture;

    private String bio;


    @PrePersist
    public void setDefaultProfilePicture() {
        if (profile_picture == null || profile_picture.length == 0) {
            try{
                this.profile_picture = Files.readAllBytes(Paths.get("src/main/resources/static/default_profile_picture.png"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
