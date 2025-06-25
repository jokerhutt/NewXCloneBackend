package com.xclone.xclone.domain.user;

import com.xclone.xclone.domain.feed.EdgeRank;
import com.xclone.xclone.domain.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestBody ArrayList<Integer> ids) {
        return ResponseEntity.ok(userService.findAllUserDTOByIds(ids));

    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUserById(@RequestParam Integer id) {
        return ResponseEntity.ok(userService.findUserByID(id));
    }

    @GetMapping("/getProfilePic")
    public ResponseEntity<String> getProfilePic(@RequestParam Integer id) {
        System.out.println("Requested pfp for: " + id);
        return ResponseEntity.ok(userService.getUserProfileMedia(id, "profilePic"));
    }

    @GetMapping("/getBannerImage")
    public ResponseEntity<String> getBannerImage(@RequestParam Integer id) {
        return ResponseEntity.ok(userService.getUserProfileMedia(id, "bannerImage"));
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User signupUser) {

        UserDTO newUser = userService.registerUser(signupUser);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.username);
        return userService.verifyUser(loginRequest);
    }

    @GetMapping("/getAdminUser")
    public ResponseEntity<UserDTO> getUser(@RequestParam Integer id) {
        System.out.println("Booyah " + id);
        userService.generateFeed(id);
        return ResponseEntity.ok(userService.findUserByID(id));
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