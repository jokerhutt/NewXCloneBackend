package com.xclone.xclone.domain.post.poll;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "poll_votes")
public class    PollVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "poll_id")
    Integer pollId;

    @Column(name = "poll_choice_id")
    Integer pollChoiceId;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "created_at", updatable = false, insertable = false)
    Timestamp createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public Integer getPollChoiceId() {
        return pollChoiceId;
    }

    public void setPollChoiceId(Integer pollChoiceId) {
        this.pollChoiceId = pollChoiceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
