package com.xclone.xclone.domain.bookmark;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("""
    SELECT b.bookmarkedPost
    FROM Bookmark b
    WHERE b.bookmarkedBy = :userId
      AND b.bookmarkedPost < :cursor
    ORDER BY b.bookmarkedPost DESC
""")
    List<Integer> findPaginatedBookmarkedPostIds(
            @Param("userId") int userId,
            @Param("cursor") long cursor,
            Pageable pageable
    );
}
