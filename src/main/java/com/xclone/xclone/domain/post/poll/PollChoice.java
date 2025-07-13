package com.xclone.xclone.domain.post.poll;

import jakarta.persistence.*;

@Entity
@Table(name = "poll_choices")
public class PollChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "choice")
    private String choice;

    @Column(name = "vote_count")
    private int voteCount;

    @Column(name = "poll_id")
    private Integer poll_id;

    public Integer getPoll_id() {
        return poll_id;
    }

    public void setPoll_id(Integer poll_id) {
        this.poll_id = poll_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String text) {
        this.choice = text;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

}

