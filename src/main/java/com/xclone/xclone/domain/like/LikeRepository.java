package com.xclone.xclone.domain.like;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findById(int id);
    ArrayList<Like> findAllByLikerId(int id);
    ArrayList<Like> findAllByLikedPostId(int id);
    boolean existsByLikerIdAndLikedPostId(Integer likerId, Integer likedPostId);
    Optional<Like> findByLikerIdAndLikedPostId(Integer likerId, Integer likedPostId);

    @Query("SELECT l.likedPostId FROM Like l JOIN Post p ON l.likedPostId = p.id WHERE l.likerId = :userId AND l.createdAt < :cursor ORDER BY l.createdAt DESC")
    List<Integer> findPaginatedLikedPostIdsByTime(@Param("userId") int userId, @Param("cursor") Timestamp cursor, Pageable pageable);
}




