package com.xclone.xclone.domain.post.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PollService {

    private final PollsRepository pollsRepository;
    private final PollChoicesRepository pollChoicesRepository;


    @Autowired
    public PollService(PollsRepository pollsRepository, PollChoicesRepository pollChoicesRepository) {
        this.pollsRepository = pollsRepository;
        this.pollChoicesRepository = pollChoicesRepository;
    }

    public void createNewPollForPost (Integer postId, List<String> pollChoices) {
        Poll poll = new Poll();
        poll.setPostId(postId);
        poll.setExpiresAt(new Timestamp(System.currentTimeMillis()));
        poll = pollsRepository.save(poll);
        if (poll == null) {throw new IllegalArgumentException("Poll could not be saved");}

        for (String pollChoice : pollChoices) {
            PollChoice pollChoiceEntity = new PollChoice();
            pollChoiceEntity.setPoll_id(poll.getId());
            pollChoiceEntity.setVoteCount(0);
            pollChoiceEntity.setChoice(pollChoice);
            pollChoicesRepository.save(pollChoiceEntity);
        }

    }


}
