package com.xclone.xclone.domain.post.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/polls")
public class PollsController {

    private final PollService pollService;

    @Autowired
    public PollsController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping("/{pollId}/choices")
    public ResponseEntity<?> getChoices(@PathVariable Integer pollId) {
        return ResponseEntity.ok(pollService.getPollChoices(pollId));
    }


}
