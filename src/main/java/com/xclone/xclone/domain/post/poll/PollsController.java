package com.xclone.xclone.domain.post.poll;

import com.xclone.xclone.domain.post.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/polls")
public class PollsController {

    private final PollService pollService;

    @Autowired
    public PollsController(PollService pollService) {
        this.pollService = pollService;
    }

    public static class VoteRequest {
        public Integer pollId;
        public Integer choiceId;
    }

    @GetMapping("/{pollId}/choices")
    public ResponseEntity<?> getChoices(@PathVariable Integer pollId) {
        System.out.println("Getting choices for poll id of " + pollId);
        return ResponseEntity.ok(pollService.getPollChoices(pollId));
    }


    @PostMapping("/submit-vote")
    public ResponseEntity<?> submitVote(@RequestBody VoteRequest voteRequest, Authentication auth) {
        Integer authUserId = (Integer) auth.getPrincipal();
        try {
            List<PollChoice> pollChoicesToReturn = pollService.submitPollVote(authUserId, voteRequest.choiceId, voteRequest.pollId);
            return ResponseEntity.ok(pollChoicesToReturn);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }

    }

    @GetMapping("/{pollId}/getPollVote")
    public ResponseEntity<?> getPollVote(@PathVariable Integer pollId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Integer authUserId = (Integer) auth.getPrincipal();

        // Call your service to fetch whether this user voted and return the choice ID (or -1 if not)
        Integer votedChoiceId = pollService.getVotedChoiceId(pollId, authUserId);

        return ResponseEntity.ok(votedChoiceId);
    }


}
