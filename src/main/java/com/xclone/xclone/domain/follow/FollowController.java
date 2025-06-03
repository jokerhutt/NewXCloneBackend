package com.xclone.xclone.domain.follow;

import com.xclone.xclone.domain.bookmark.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/followUser")
    public ResponseEntity<?> createFollow (@RequestBody NewFollow newFollow) {

        if (followService.)

    }




}
