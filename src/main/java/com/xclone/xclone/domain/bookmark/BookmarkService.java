package com.xclone.xclone.domain.bookmark;

import com.xclone.xclone.domain.post.PostDTO;
import com.xclone.xclone.domain.post.PostService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class BookmarkService {

    private final PostService postService;
    private BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkService(BookmarkRepository bookmarkRepository, PostService postService) {
        this.bookmarkRepository = bookmarkRepository;
        this.postService = postService;
    }

    public ArrayList<Integer> getAllUserBookmarkedIds(Integer userId) {
        ArrayList<Integer> bookmarkIds = new ArrayList<>();
        ArrayList<Bookmark> bookmarks =  bookmarkRepository.findAllByBookmarkedBy(userId);
        for (Bookmark bookmark : bookmarks) {
            bookmarkIds.add(bookmark.getBookmarkedPost());
        }
        return bookmarkIds;
    }

    @Transactional
    public PostDTO addNewBookmark(Integer userId, Integer bookmarkedPost) {

        if (bookmarkRepository.existsByBookmarkedByAndBookmarkedPost(userId, bookmarkedPost)) {
            throw new IllegalStateException("Bookmark exists");
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setBookmarkedBy(userId);
        bookmark.setBookmarkedPost(bookmarkedPost);

        bookmarkRepository.save(bookmark);

        PostDTO postDTO = postService.findPostDTOById(bookmarkedPost);
        if (postDTO == null) throw new IllegalStateException("Post does not exist exists");

        return postDTO;
    }

    @Transactional
    public PostDTO deleteBookmark(Integer userId, Integer bookmarkedPost) {

        Optional<Bookmark> toDelete = bookmarkRepository.findByBookmarkedByAndBookmarkedPost(userId, bookmarkedPost);
        if (!toDelete.isPresent()) throw new IllegalStateException("Bookmark does not exist");

        bookmarkRepository.delete(toDelete.get());
        PostDTO postDTO = postService.findPostDTOById(bookmarkedPost);
        if (postDTO == null) throw new IllegalStateException("Post does not exist exists");

        return postDTO;

    }

}
