package com.xclone.xclone.domain.like;
import com.xclone.xclone.domain.bookmark.Bookmark;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findById(int id);
    ArrayList<Like> findAllByLikerId(int id);
    ArrayList<Like> findAllByLikedPostId(int id);
    boolean existsByLikerIdAndLikedPostId(Integer likerId, Integer likedPostId);
    Optional<Like> findByLikerIdAndLikedPostId(Integer likerId, Integer likedPostId);
}




