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
}