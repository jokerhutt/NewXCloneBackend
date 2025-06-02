package com.xclone.xclone.domain.post;

import java.util.ArrayList;

public class PostDTO {

    public Integer id;
    public Integer userId;
    public String text;
    public ArrayList<Integer> likedBy;

    public PostDTO(Post post, ArrayList<Integer> likedBy) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.text = post.getText();
        this.likedBy = likedBy;
    }
}
