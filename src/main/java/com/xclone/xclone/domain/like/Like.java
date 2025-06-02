package com.xclone.xclone.domain.like;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "liker_id")
    private Integer likerId;

    @Column(name = "liked_post_id")
    private Integer likedPostId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLikerId() {
        return likerId;
    }

    public void setLikerId(Integer likerId) {
        this.likerId = likerId;
    }

    public Integer getLikedPostId() {
        return likedPostId;
    }

    public void setLikedPostId(Integer likedPostId) {
        this.likedPostId = likedPostId;
    }
}
