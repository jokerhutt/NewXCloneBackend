package com.xclone.xclone.domain.like;
import com.xclone.xclone.domain.bookmark.NewBookmark;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/createLike")
    public ResponseEntity<?> createLike(@RequestBody NewLike newLike) {
        try {
            likeService.addNewLike(newLike.getLikerId(), newLike.getLikedPostId());
            return ResponseEntity.ok(Map.of("message", "Like created"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/deleteLike")
    public ResponseEntity<?> removeLike(@RequestBody NewLike newLike) {

        try {
            likeService.deleteLike(newLike.getLikerId(), newLike.getLikedPostId());
            return ResponseEntity.ok(Map.of("message", "Like removed"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }


    }



}
