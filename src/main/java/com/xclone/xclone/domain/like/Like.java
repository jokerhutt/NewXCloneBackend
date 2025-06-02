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


}
