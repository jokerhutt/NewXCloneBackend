package com.xclone.xclone.domain.like;
import com.xclone.xclone.domain.bookmark.NewBookmark;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/createLike")
    public ResponseEntity<?> createLike(@RequestBody NewLike newLike) {

        return ResponseEntity.ok(likeService.addNewLike(newLike.getLikerId(), newLike.getLikedPostId()));

    }

    @PostMapping("/deleteLike")
    public ResponseEntity<?> removeLike(@RequestBody NewLike newLike) {

        return ResponseEntity.ok(likeService.deleteLike(newLike.getLikerId(), newLike.getLikedPostId()));


    }



}
