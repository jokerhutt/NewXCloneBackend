package com.xclone.xclone.domain.bookmark;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Book;
import java.sql.Timestamp;
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
JOIN Post p ON b.bookmarkedPost = p.id
WHERE b.bookmarkedBy = :userId
  AND b.createdAt < :cursor
ORDER BY b.createdAt DESC
""")
    List<Integer> findPaginatedBookmarkedPostIdsByTime(
            @Param("userId") int userId,
            @Param("cursor") Timestamp cursor,
            Pageable pageable
    );
}
