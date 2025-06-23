package com.xclone.xclone.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(int id);
    Optional<Post> findByUserId(int id);
    Optional<List<Post>> findAllByUserId(int id);
    ArrayList<Post> findAllByParentId(Integer id);

    @Query("SELECT p.id FROM Post p WHERE p.parentId IS NULL AND p.id < :cursor ORDER BY p.id DESC")
    List<Integer> findNextPaginatedPostIds(@Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT p.id FROM Post p WHERE p.userId = :userId AND p.parentId IS NULL AND p.id < :cursor ORDER BY p.id DESC")
    List<Integer> findPaginatedTweetIdsByUserId(@Param("userId") int userId, @Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND p.parentId IS NULL AND p.id < :cursor ORDER BY p.id DESC")
    List<Post> findPaginatedTweetsByUserId(@Param("userId") int userId, @Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT p.id FROM Post p WHERE p.userId = :userId AND p.parentId IS NOT NULL AND p.id < :cursor ORDER BY p.id DESC")
    List<Integer> findPaginatedReplyIdsByUserId(@Param("userId") int userId, @Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT p.id FROM Post p WHERE p.userId IN :followedUserIds AND p.parentId IS NULL AND p.id < :cursor ORDER BY p.id DESC")
    List<Integer> findPaginatedPostIdsFromFollowedUsers(@Param("followedUserIds") List<Integer> followedUserIds, @Param("cursor") int cursor, Pageable pageable);

    @Query("SELECT p.id FROM Post p")
    Page<Integer> findAllPostIds(Pageable pageable);

    @Query("SELECT p.id FROM Post p WHERE p.userId = :authorId AND p.parentId IS NULL")
    List<Integer> findPostIdsByAuthor(@Param("authorId") int authorId);

    @Query("SELECT p FROM Post p WHERE p.parentId IS NULL")
    List<Post> findAllTopLevelPosts();

    @Query("""
    SELECT p.id FROM Post p
    WHERE p.userId = :userId
      AND p.id < :cursor
      AND p.id IN (
        SELECT DISTINCT pm.postId FROM PostMedia pm
      )
    ORDER BY p.id DESC
""")
    List<Integer> findPaginatedPostIdsWithMediaByUserId(
            @Param("userId") int userId,
            @Param("cursor") int cursor,
            Pageable pageable
    );

}