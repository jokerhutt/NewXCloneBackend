package com.xclone.xclone.domain.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;

public class UserDTO {

    public Integer id;
    public String username;
    public String email;
    public String bio;
    public String profilePicture;
    public String bannerImage;
    public String displayName;
    public ArrayList<Integer> posts;
    public ArrayList<Integer> bookmarkedPosts;
    public ArrayList<Integer> likedPosts;
    public ArrayList<Integer> followers;
    public ArrayList<Integer> following;
    public Timestamp createdAt;
    public ArrayList<Integer> replies;


    public UserDTO(User user, ArrayList<Integer> posts, ArrayList<Integer> bookmarkedPosts, ArrayList<Integer> likedPosts, ArrayList<Integer> followers, ArrayList<Integer> following, ArrayList<Integer> replies) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.displayName = user.getDisplayName();
        this.profilePicture = Base64.getEncoder().encodeToString(user.getProfilePicture());
        this.bannerImage = Base64.getEncoder().encodeToString(user.getBannerImage());
        this.posts = posts;
        this.bookmarkedPosts = bookmarkedPosts;
        this.likedPosts = likedPosts;
        this.followers = followers;
        this.following = following;
        this.createdAt = user.getCreatedAt();
        this.replies = replies;

    }
}