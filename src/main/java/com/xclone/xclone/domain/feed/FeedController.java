package com.xclone.xclone.domain.feed;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    FeedService feedService;

    public FeedController (FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/getFeedPage")
    public ResponseEntity<?> getFeedPage(
            @RequestParam String type,
            @RequestParam(defaultValue = "0") int cursor,
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(feedService.getPaginatedPostIds(cursor, limit, userId, type));
    }

}
