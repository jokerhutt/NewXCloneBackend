package com.xclone.xclone.domain.bookmark;

import com.xclone.xclone.domain.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.ok(bookmarkService.addNewBookmark(newBookmark.getBookmarkedBy(), newBookmark.getBookmarkedPost()));

    }

    @PostMapping("/deleteBookmark")
    public ResponseEntity<?> deleteBookmark(@RequestBody NewBookmark newBookmark) {

        return ResponseEntity.ok(bookmarkService.deleteBookmark(newBookmark.getBookmarkedBy(), newBookmark.getBookmarkedPost()));

    }





}
