package com.xclone.xclone.domain.post;

import com.xclone.xclone.domain.notification.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PostController(PostService postService, NotificationService notificationService) {
        this.postService = postService;
        this.notificationService = notificationService;
    }

    @PostMapping("/createPostOld")
    public ResponseEntity<?> createPost(@RequestBody NewPost newPost) {

        PostDTO createdPost = postService.createNewPost(newPost);
        PostDTO parentPost = null;
        if (newPost.parentId != null) {
            parentPost = postService.findPostDTOById(newPost.parentId);
        }
        ArrayList<PostDTO> postsToReturn = new ArrayList<>();
        postsToReturn.add(createdPost);
        postsToReturn.add(parentPost);
        return ResponseEntity.ok(postsToReturn);

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

        String base64 = Base64.getEncoder().encodeToString(media.getData());

        Map<String, String> response = new HashMap<>();
        response.put("src", "data:" + media.getMimeType() + ";base64," + base64);
        response.put("alt", media.getFileName());
        response.put("type", media.getMimeType());

        return ResponseEntity.ok(response);
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
            notificationService.handlePostCreateNotification(userId, post.getId(), "reply");
        }

        List<PostDTO> postsToReturn = new ArrayList<>();
        postsToReturn.add(postService.findPostDTOById(post.getId()));
        if (parentId != null) {
            postsToReturn.add(postService.findPostDTOById(parentId));
        }

        return ResponseEntity.ok(postsToReturn);
    }

}
