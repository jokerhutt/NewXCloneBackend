package com.xclone.xclone.domain.post;

import com.xclone.xclone.domain.bookmark.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface PostMediaRepository extends JpaRepository<PostMedia, Integer> {
    ArrayList<PostMedia> findAllByPostId(Integer postId);
    List<PostMedia> findAllByPostIdIn(List<Integer> postIds);

}
