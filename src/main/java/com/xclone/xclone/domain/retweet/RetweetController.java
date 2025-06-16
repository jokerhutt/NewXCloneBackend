package com.xclone.xclone.domain.retweet;

import com.xclone.xclone.domain.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/retweets")
public class RetweetController {

    private final RetweetService retweetService;
    private final PostService postService;

    public RetweetController(RetweetService retweetService, PostService postService) {
        this.retweetService = retweetService;
        this.postService = postService;
    }

    @PostMapping("/newRetweet")
    public ResponseEntity<?> newRetweet(@RequestBody NewRetweet newRetweet) {
        try {
            retweetService.createRetweet(newRetweet);
            return ResponseEntity.ok(Map.of("message", "Retweet created"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/deleteRetweet")
    public ResponseEntity<?> deleteRetweet(@RequestBody NewRetweet retweet) {
        try {
            retweetService.deleteRetweet(retweet);
            return ResponseEntity.ok(Map.of("message", "Retweet removed"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

}
