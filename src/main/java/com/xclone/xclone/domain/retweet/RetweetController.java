package com.xclone.xclone.domain.retweet;

import com.xclone.xclone.domain.post.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println("Controller received like request");
        retweetService.createRetweet(newRetweet);
        return ResponseEntity.ok(postService.findPostDTOById(newRetweet.referenceId));
    }

    @PostMapping("/deleteRetweet")
    public ResponseEntity<?> deleteRetweet(@RequestBody NewRetweet retweet) {
        System.out.println("Controller received delete request");
        retweetService.deleteRetweet(retweet);
        return ResponseEntity.ok(postService.findPostDTOById(retweet.referenceId));
    }

}
