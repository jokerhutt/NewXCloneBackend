package com.xclone.xclone.domain.user;

import com.xclone.xclone.domain.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, PostService postService, UserRepository userRepository) {
        this.userService = userService;
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUserById(@RequestParam Integer id) {
        return ResponseEntity.ok(userService.generateUserDTOByUserId(id));
    }

    @PostMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestBody ArrayList<Integer> ids) {
        return ResponseEntity.ok(userService.findAllUserDTOByIds(ids));
    }

    @GetMapping("/getProfilePic")
    public ResponseEntity<String> getProfilePic(@RequestParam Integer id) {
        System.out.println("Requested pfp for: " + id);
        return ResponseEntity.ok(userService.getUserProfileMedia(id, "profilePic"));
    }

    @GetMapping("/getTopFiveUsers")
    public ResponseEntity<?> getTopFiveUsers() {
        return ResponseEntity.ok(userRepository.findUserIdsByFollowerCount(99999, 4));
    }

    @GetMapping("/getBannerImage")
    public ResponseEntity<String> getBannerImage(@RequestParam Integer id) {
        return ResponseEntity.ok(userService.getUserProfileMedia(id, "bannerImage"));
    }

    @GetMapping("/getAdminUser")
    public ResponseEntity<UserDTO> getUser(@RequestParam Integer id) {
        System.out.println("Booyah " + id);
        userService.generateFeed(id);
        return ResponseEntity.ok(userService.generateUserDTOByUserId(id));
    }

    @GetMapping("/searchUsers")
    public List<Integer> searchUsers(@RequestParam String q) {
        return userService.searchUsersByName(q);
    }

    @GetMapping("/getDiscoverFeed")
    public ResponseEntity<?> getFeedPage(
            @RequestParam(defaultValue = "0") long cursor,
            @RequestParam(defaultValue = "10") int limit
    ) {
        System.out.println("Received request for cursor: " + cursor + " limit " + limit);
        return ResponseEntity.ok(userService.getPaginatedTopUsers(cursor, limit));
    }



}