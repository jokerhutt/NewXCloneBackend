package com.xclone.xclone.domain.bookmark;

import jakarta.persistence.*;

@Entity
@Table (name = "bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bookmarked_by")
    private Integer bookmarkedBy;

    @Column(name = "bookmarked_post")
    private Integer bookmarkedPost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookmarkedBy() {
        return bookmarkedBy;
    }

    public void setBookmarkedBy(Integer bookmarkedBy) {
        this.bookmarkedBy = bookmarkedBy;
    }

    public Integer getBookmarkedPost() {
        return bookmarkedPost;
    }

    public void setBookmarkedPost(Integer bookmarkedPost) {
        this.bookmarkedPost = bookmarkedPost;
    }
}
