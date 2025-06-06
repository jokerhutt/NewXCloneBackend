package com.xclone.xclone.domain.follow;

import com.xclone.xclone.domain.bookmark.BookmarkService;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        UserDTO firstDTO = userService.findUserByID(newFollow.followerId);
        if (firstDTO != null) {
            System.out.println("<FIRST> UserDTO followers: " + firstDTO.followers);
        }
        Integer addedFollowId = followService.addNewFollow(newFollow.followerId, newFollow.followedId);
        UserDTO newFollowedUser = userService.findUserByID(addedFollowId);
        if (newFollowedUser != null) {
            System.out.println("📦 UserDTO followers: " + newFollowedUser.followers);
        }
        return ResponseEntity.ok(newFollowedUser);

    }

    @PostMapping("/unfollowUser")
    public ResponseEntity<?> unfollowUser (@RequestBody NewFollow newFollow) {
        UserDTO firstDTO = userService.findUserByID(newFollow.followerId);
        if (firstDTO != null) {
            System.out.println("<FIRST> UserDTO followers: " + firstDTO.followers);
        }
        Integer removedFollowId = followService.deleteFollow(newFollow.followerId, newFollow.followedId);
        UserDTO removedFollowedUser = userService.findUserByID(removedFollowId);
        if (removedFollowedUser != null) {
            System.out.println("📦 UserDTO followers: " + removedFollowedUser.followers);
        }
        return ResponseEntity.ok(removedFollowedUser);
    }




}
