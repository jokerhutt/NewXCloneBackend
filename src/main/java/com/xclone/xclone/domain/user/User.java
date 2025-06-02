package com.xclone.xclone.domain.user;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 64, name = "name")
    private String username;

    @Column(nullable = false, length = 400)
    private String password;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @Column(nullable = false, length = 45, name = "display_name")
    private String displayName;

    @Column (nullable = false, name = "profile_picture")
    @Lob
    private byte[] profilePicture;

    @Column (nullable = false, name = "banner_image")
    @Lob
    private byte[] bannerImage;

    @Column(length = 180)
    private String bio;

    @Column(nullable = false)
    private java.sql.Timestamp createdAt;


    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public byte[] getBannerImage() {
        return bannerImage;
    }

    public String getBio() {
        return bio;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setBannerImage(byte[] bannerImage) {
        this.bannerImage = bannerImage;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
