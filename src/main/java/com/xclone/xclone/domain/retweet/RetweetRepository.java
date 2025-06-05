package com.xclone.xclone.domain.retweet;

import com.xclone.xclone.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetweetRepository extends JpaRepository<Retweet, Integer> {



}
