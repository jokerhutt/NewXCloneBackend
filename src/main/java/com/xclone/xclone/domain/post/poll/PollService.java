package com.xclone.xclone.domain.post.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollService {

    private final PollsRepository pollsRepository;

    @Autowired
    public PollService(PollsRepository pollsRepository) {
        this.pollsRepository = pollsRepository;
    }

    public void createNewPollForPost (Integer postId, List<String> pollChoices) {



    }


}
