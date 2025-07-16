package com.xclone.xclone.domain.post.poll;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PollService {

    private final PollsRepository pollsRepository;
    private final PollChoicesRepository pollChoicesRepository;
    private final PollVotesRepository pollVotesRepository;


    @Autowired
    public PollService(PollsRepository pollsRepository, PollChoicesRepository pollChoicesRepository, PollVotesRepository pollVotesRepository) {
        this.pollsRepository = pollsRepository;
        this.pollChoicesRepository = pollChoicesRepository;
        this.pollVotesRepository = pollVotesRepository;
    }

    public void createNewPollForPost (Integer postId, List<String> pollChoices) {
        Poll poll = new Poll();
        poll.setPostId(postId);
        poll.setExpiresAt(new Timestamp(System.currentTimeMillis()));
        poll = pollsRepository.save(poll);
        if (poll == null) {throw new IllegalArgumentException("Poll could not be saved");}

        for (String pollChoice : pollChoices) {
            PollChoice pollChoiceEntity = new PollChoice();
            pollChoiceEntity.setPollId(poll.getId());
            pollChoiceEntity.setVoteCount(0);
            pollChoiceEntity.setChoice(pollChoice);
            pollChoicesRepository.save(pollChoiceEntity);
        }
    }

    @Transactional
    public List<PollChoice> submitPollVote(Integer voterId, Integer choiceId, Integer pollId) {

        boolean hasVoted = pollVotesRepository.existsByUserIdAndPollId(voterId, pollId);
        if (hasVoted) {
            throw new IllegalStateException("User has already voted in this poll");
        }

        PollVote vote = new PollVote();
        vote.setUserId(voterId);
        vote.setPollChoiceId(choiceId);
        vote.setPollId(pollId);
        pollVotesRepository.save(vote);

        PollChoice selectedChoice = pollChoicesRepository.findById(choiceId)
                .orElseThrow(() -> new EntityNotFoundException("Poll choice not found"));
        selectedChoice.setVoteCount(selectedChoice.getVoteCount() + 1);
        pollChoicesRepository.save(selectedChoice);

        return pollChoicesRepository.findAllByPollId(pollId);
    }

    public int getVotedChoiceId(Integer pollId, Integer userId) {
        Optional<PollVote> pollVote = pollVotesRepository.findByPollIdAndUserId(pollId, userId);
        if (pollVote.isPresent()) {
            return pollVote.get().pollChoiceId;
        } else {
            return -1;
        }
    }

    public List<PollChoice> getPollChoices (Integer pollId) {
        return pollChoicesRepository.findAllByPollId(pollId);
    }


}
