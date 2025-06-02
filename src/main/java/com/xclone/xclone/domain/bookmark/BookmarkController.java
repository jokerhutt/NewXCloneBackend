package com.xclone.xclone.domain.bookmark;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/createBookmark")
    public ResponseEntity<?> createBookmark(@RequestBody NewBookmark newBookmark) {

        if (bookmarkService.addNewBookmark(newBookmark.getBookmarkedBy(), newBookmark.getBookmarkedPost())) {
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("NOTSUCCESS");
        }

    }

    @PostMapping("/deleteBookmark")
    public ResponseEntity<?> deleteBookmark(@RequestBody NewBookmark newBookmark) {

        if (bookmarkService.deleteBookmark(newBookmark.getBookmarkedBy(), newBookmark.getBookmarkedPost())) {
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("NOTSUCCESS");
        }

    }





}
