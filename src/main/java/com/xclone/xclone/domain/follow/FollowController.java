package com.xclone.xclone.domain.follow;

import com.xclone.xclone.domain.bookmark.BookmarkService;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    public FollowController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @PostMapping("/followUser")
    public ResponseEntity<?> createFollow (@RequestBody NewFollow newFollow) {
        try {
            UserDTO followedUserToReturn = followService.addNewFollow(newFollow.followerId, newFollow.followedId);
            return ResponseEntity.ok(followedUserToReturn);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/unfollowUser")
    public ResponseEntity<?> unfollowUser (@RequestBody NewFollow newFollow) {
        try {
            UserDTO followedUserToReturn = followService.deleteFollow(newFollow.followerId, newFollow.followedId);
            return ResponseEntity.ok(followedUserToReturn);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }




}
