package com.xclone.xclone.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(int id);
    Optional<Post> findByUserId(int id);
    Optional<List<Post>> findAllByUserId(int id);
    ArrayList<Post> findAllByParentId(Integer id);

    @Query("""
    SELECT p.id FROM Post p
    WHERE p.id IN (
        SELECT p1.id FROM Post p1
        WHERE p1.userId = :userId AND p1.parentId IS NULL
    
        UNION
    
        SELECT r.referenceId FROM Retweet r
        JOIN Post p2 ON p2.id = r.referenceId
        WHERE r.retweeterId = :userId AND p2.parentId IS NULL
    )
    AND p.id < :cursor
    ORDER BY p.id DESC
    """)
    List<Integer> findPaginatedTweetAndRetweetIdsByUserId(
            @Param("userId") int userId,
            @Param("cursor") Timestamp cursor,
            Pageable pageable
    );


    @Query(value = """
    SELECT post_id FROM (
      SELECT p.id AS post_id, p.created_at AS activity_time
      FROM posts p
      WHERE p.user_id = :userId
        AND p.parent_id IS NULL
        AND p.created_at <= :cursor

      UNION ALL

      SELECT r.reference_id AS post_id, r.created_at
      FROM retweets r
      JOIN posts p ON p.id = r.reference_id
      WHERE r.retweeter_id = :userId
        AND p.user_id != :userId
        AND r.created_at <= :cursor
    ) AS combined
    ORDER BY activity_time DESC
    """,
            nativeQuery = true)
    List<Integer> findPostIdsByUserAndReposts(
            @Param("userId") int userId,
            @Param("cursor") Timestamp cursor,
            Pageable pageable
    );



        @Query("""
    SELECT p.id
    FROM Post p
    WHERE p.parentId IS NULL AND p.createdAt < :cursor
    ORDER BY p.createdAt DESC
    """)
    List<Integer> findNextPaginatedPostIdsByTime(@Param("cursor") Timestamp cursor, Pageable pageable);

    @Query("SELECT p.id FROM Post p WHERE p.userId = :userId AND p.parentId IS NULL AND p.id < :cursor ORDER BY p.id DESC")
    List<Integer> findPaginatedTweetIdsByUserId(@Param("userId") int userId, @Param("cursor") long cursor, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND p.parentId IS NULL AND p.id < :cursor ORDER BY p.id DESC")
    List<Post> findPaginatedTweetsByUserId(@Param("userId") int userId, @Param("cursor") long cursor, Pageable pageable);

    @Query("""
    SELECT p.id
    FROM Post p
    WHERE p.userId = :userId
      AND p.parentId IS NOT NULL
      AND p.createdAt < :cursor
    ORDER BY p.createdAt DESC
    """)
    List<Integer> findPaginatedReplyIdsByUserIdByTime(
            @Param("userId") int userId,
            @Param("cursor") Timestamp cursor,
            Pageable pageable
    );

    @Query("""
    SELECT p.id
    FROM Post p
    WHERE p.userId IN :followedUserIds
      AND p.parentId IS NULL
      AND p.createdAt < :cursor
    ORDER BY p.createdAt DESC
    """)
    List<Integer> findPaginatedPostIdsFromFollowedUsersByTime(
            @Param("followedUserIds") List<Integer> followedUserIds,
            @Param("cursor") Timestamp cursor,
            Pageable pageable
    );
    @Query("SELECT p.id FROM Post p")
    Page<Integer> findAllPostIds(Pageable pageable);

    @Query("SELECT p.id FROM Post p WHERE p.userId = :authorId AND p.parentId IS NULL")
    List<Integer> findPostIdsByAuthor(@Param("authorId") int authorId);

    @Query("SELECT p FROM Post p WHERE p.parentId IS NULL")
    List<Post> findAllTopLevelPosts();

    @Query("""
    SELECT p.id
    FROM Post p
    WHERE p.userId = :userId
      AND p.createdAt < :cursor
      AND EXISTS (
        SELECT 1 FROM PostMedia pm WHERE pm.postId = p.id
      )
    ORDER BY p.createdAt DESC
    """)
    List<Integer> findPaginatedPostIdsWithMediaByUserIdByTime(
            @Param("userId") int userId,
            @Param("cursor") Timestamp cursor,
            Pageable pageable
    );

}