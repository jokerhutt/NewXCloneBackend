package com.xclone.xclone.domain.bookmark;

import com.xclone.xclone.domain.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final PostService postService;

    public BookmarkController(BookmarkService bookmarkService, PostService postService) {
        this.bookmarkService = bookmarkService;
        this.postService = postService;
    }

    @PostMapping("/createBookmark")
    public ResponseEntity<?> createBookmark(@RequestBody NewBookmark newBookmark) {
        try {
            bookmarkService.addNewBookmark(newBookmark.getBookmarkedBy(), newBookmark.getBookmarkedPost());
            return ResponseEntity.ok(Map.of("message", "Bookmark created"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }

    }

    @PostMapping("/deleteBookmark")
    public ResponseEntity<?> deleteBookmark(@RequestBody NewBookmark newBookmark) {

        return ResponseEntity.ok(bookmarkService.deleteBookmark(newBookmark.getBookmarkedBy(), newBookmark.getBookmarkedPost()));

    }





}
