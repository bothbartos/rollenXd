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
    private byte[] profilePicture;

    private String bio;


    public void setDefaultProfilePicture() {
        if (profilePicture == null || profilePicture.length == 0) {
            try{
                this.profilePicture = Files.readAllBytes(Paths.get("src/main/resources/static/default_profile_picture.png"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
