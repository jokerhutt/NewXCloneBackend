package com.xclone.xclone.domain.post;

import com.xclone.xclone.domain.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    private final NotificationService notificationService;
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostService postService, NotificationService notificationService, PostRepository postRepository) {
        this.postService = postService;
        this.notificationService = notificationService;
        this.postRepository = postRepository;
    }

    @PostMapping("/getPosts")
    public ResponseEntity<?> getPost(@RequestBody ArrayList<Integer> ids) {
        System.out.println("Received request to retrieve posts");
        return ResponseEntity.ok(postService.findAllPostDTOByIds(ids));

    }

    @GetMapping("/getAllPostMediaByPostId")
    public ResponseEntity<?> getPostMedias(@RequestBody ArrayList<Integer> ids) {
        return ResponseEntity.ok(postService.getAllPostMediaByPostId(ids));
    }

    //TODO cleanup
    @GetMapping("/getPostMediaById")
    public ResponseEntity<?> getPostMedia(@RequestParam Integer id) {
        PostMedia media = postService.getPostMediaById(id);
        if (media == null || media.getData() == null) {
            return ResponseEntity.notFound().build();
        }

        Map encodedPostMedia = postService.preparePostMediaMapToBase64(media);

        return ResponseEntity.ok(encodedPostMedia);
    }

    @GetMapping("/getForYouFeed")
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Integer> postPage = postRepository.findAllPostIds(pageable);
        List<Integer> ids = postPage.getContent();
        return ResponseEntity.ok(ids);
    }

    @GetMapping("/getSinglePost/{id}")
    public ResponseEntity<?> getSinglePost(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.findPostDTOById(id));
    }

    @GetMapping("/getAllPostIds")
    public ResponseEntity<?> getAllPostIds() {
        return ResponseEntity.ok(postService.findAllPostIds());
    }

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(
            @RequestParam("userId") Integer userId,
            @RequestParam("text") String text,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        Post post = postService.createPostEntity(userId, text, parentId);

        if (images != null && !images.isEmpty()) {
            postService.savePostImages(post.getId(), images);
        }

        if (parentId != null) {
            notificationService.createNotificationFromType(userId, post.getId(), "reply");
        }

        return ResponseEntity.ok(Map.of("message", "Post created successfully"));
    }

}
