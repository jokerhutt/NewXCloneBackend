package com.xclone.xclone.domain.post.poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollChoicesRepository extends JpaRepository<PollChoice, Integer> {
    List<PollChoice> findAllByPollId(Integer pollId);
}
