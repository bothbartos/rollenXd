package com.codecool.tavirutyutyu.zsomlexd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

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

    @ElementCollection(fetch = FetchType.EAGER) // Stores roles in a separate table
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING) // Store roles as Strings
    @Column(name = "role")
    private Set<Role> roles;

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
