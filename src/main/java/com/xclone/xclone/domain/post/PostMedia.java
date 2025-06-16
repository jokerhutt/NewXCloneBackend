package com.xclone.xclone.domain.post;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "post_media")
public class PostMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer postId;
    private String fileName;
    private String mimeType;

    @Lob
    @Column(length = 16777215) // MEDIUMBLOB size
    private byte[] data;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    public PostMedia() {}

    public PostMedia(Integer postId, String fileName, String mimeType, byte[] data) {
        this.postId = postId;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPostId() {
        return postId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public byte[] getData() {
        return data;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}