package com.xclone.xclone.domain.bookmark;

import com.xclone.xclone.domain.post.PostDTO;
import com.xclone.xclone.domain.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBookmark(
            @RequestBody NewBookmark newBookmark,
            Authentication auth
    ) {
        Integer authUserId = (Integer) auth.getPrincipal();
        try {
            PostDTO bookmarkToReturn = bookmarkService.addNewBookmark(authUserId, newBookmark.getBookmarkedPost());
            return ResponseEntity.ok(bookmarkToReturn);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBookmark(
            @RequestBody NewBookmark newBookmark,
            Authentication auth
    ) {
        Integer authUserId = (Integer) auth.getPrincipal();
        return ResponseEntity.ok(
                bookmarkService.deleteBookmark(authUserId, newBookmark.getBookmarkedPost())
        );
    }





}
