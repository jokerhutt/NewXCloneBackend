package com.xclone.xclone.domain.post.poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollsRepository extends JpaRepository<Poll, Integer> {

    boolean existsByPostId(Integer postId);

    Optional<Poll> findByPostId(Integer postId);



}
