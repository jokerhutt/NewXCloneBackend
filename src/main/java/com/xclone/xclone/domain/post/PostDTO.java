package com.xclone.xclone.domain.post;

import java.util.ArrayList;

public class PostDTO {

    public Integer id;
    public Integer userId;
    public String text;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.text = post.getText();
    }
}
