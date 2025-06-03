package com.xclone.xclone.domain.bookmark;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    Optional<Bookmark> findById(int id);
    ArrayList<Bookmark> findAllByBookmarkedBy(int id);
    ArrayList<Bookmark> findAllByBookmarkedPost(Integer bookmarkedPost);
    boolean existsByBookmarkedByAndBookmarkedPost(Integer bookmarkedBy, Integer bookmarkedPost);
    Optional<Bookmark> findByBookmarkedByAndBookmarkedPost(Integer bookmarkedBy, Integer bookmarkedPost);
}
