package com.xclone.xclone.domain.follow;

import com.xclone.xclone.domain.bookmark.BookmarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final BookmarkService bookmarkService;

    public FollowController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }




}
