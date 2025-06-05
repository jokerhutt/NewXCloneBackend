package com.xclone.xclone.domain.post;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PostDTO {

    public Integer id;
    public Integer userId;
    public String text;
    public Timestamp createdAt;
    public ArrayList<Integer> likedBy;
    public ArrayList<Integer> bookmarkedBy;
    public Integer parentId;

    public PostDTO(Post post, ArrayList<Integer> likedBy, ArrayList<Integer> bookmarkedBy) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.text = post.getText();
        this.likedBy = likedBy;
        this.bookmarkedBy = bookmarkedBy;
        this.createdAt = post.getCreatedAt();
        this.parentId = post.getParentId();
    }
}
