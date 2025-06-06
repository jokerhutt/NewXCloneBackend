package com.xclone.xclone.domain.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestBody NewPost newPost) {

        PostDTO createdPost = postService.createNewPost(newPost);
        return ResponseEntity.ok(createdPost);

    }

    @PostMapping("/getPost")
    public ResponseEntity<?> getPost(@RequestBody ArrayList<Integer> ids) {
        return ResponseEntity.ok(postService.findAllPostDTOByIds(ids));

    }

    @GetMapping("/getSinglePost/{id}")
    public ResponseEntity<?> getSinglePost(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.findPostDTOById(id));
    }

    @GetMapping("/getAllPostIds")
    public ResponseEntity<?> getAllPostIds() {
        return ResponseEntity.ok(postService.findAllPostIds());
    }

}
