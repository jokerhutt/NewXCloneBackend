package com.xclone.xclone.domain.post;

import com.xclone.xclone.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(int id);
    Optional<Post> findByUserId(int id);
    Optional<List<Post>> findAllByUserId(int id);
    ArrayList<Post> findAllByParentId(Integer id);

    @Query("SELECT p.id FROM Post p")
    Page<Integer> findAllPostIds(Pageable pageable);
}