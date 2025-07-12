package com.xclone.xclone.domain.post.poll;

import java.sql.Timestamp;
import java.util.List;

public class PollDTO {
    private Long pollId;
    private Long postId;
    private boolean multiVote;
    private Timestamp expiresAt;
    private List<PollChoiceDTO> choices;
    private List<Integer> userVoteChoiceIds; //OPTIONAL FOR CURR USER

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public boolean isMultiVote() {
        return multiVote;
    }

    public void setMultiVote(boolean multiVote) {
        this.multiVote = multiVote;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public List<PollChoiceDTO> getChoices() {
        return choices;
    }

    public void setChoices(List<PollChoiceDTO> choices) {
        this.choices = choices;
    }

    public List<Integer> getUserVoteChoiceIds() {
        return userVoteChoiceIds;
    }

    public void setUserVoteChoiceIds(List<Integer> userVoteChoiceIds) {
        this.userVoteChoiceIds = userVoteChoiceIds;
    }
}