package com.xclone.xclone.domain.post.poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollVotesRepository extends JpaRepository<PollVote, Integer> {

    boolean existsByUserIdAndPollId(Integer userId, Integer pollId);

    Optional<PollVote> findByPollIdAndUserId(Integer pollId, Integer userId);
}
