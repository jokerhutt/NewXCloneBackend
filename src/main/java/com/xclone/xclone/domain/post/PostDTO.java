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
    public ArrayList<Integer> replies;
    public Integer parentId;
    public ArrayList<Integer> retweetedBy;
    public ArrayList<PostMedia> postMedia;
    public Integer pollId;
    public Timestamp pollExpiryTimeStamp;

    public PostDTO(Post post, ArrayList<Integer> likedBy, ArrayList<Integer> bookmarkedBy, ArrayList<Integer> replies, ArrayList<Integer> retweetedBy, ArrayList<PostMedia> postMedia, Integer pollId, Timestamp pollExpiryTimeStamp) {
        this.id = post.getId();
        this.userId = post.getUserId();
        this.text = post.getText();
        this.likedBy = likedBy;
        this.bookmarkedBy = bookmarkedBy;
        this.createdAt = post.getCreatedAt();
        this.parentId = post.getParentId();
        this.replies = replies;
        this.retweetedBy = retweetedBy;
        this.postMedia = postMedia;
        this.pollId = pollId;
        this.pollExpiryTimeStamp = pollExpiryTimeStamp;
    }



}
