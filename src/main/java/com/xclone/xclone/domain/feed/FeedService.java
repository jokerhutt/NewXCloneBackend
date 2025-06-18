package com.xclone.xclone.domain.feed;

import com.xclone.xclone.domain.like.LikeRepository;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.post.PostRepository;
import com.xclone.xclone.domain.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedService {

    private final LikeRepository likeRepository;
    PostRepository postRepository;

    @Autowired
    public FeedService(PostRepository postRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    public Map<String, Object> getPaginatedPostIds(int cursor, int limit, Integer userId, String type) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("id").descending());
        List<Integer> ids = getPaginatedFeed(type, userId, cursor, pageable);

        Integer nextCursor = ids.size() < limit ? null : ids.get(ids.size() - 1);

        Map<String, Object> response = new HashMap<>();
        response.put("posts", ids);
        response.put("nextCursor", nextCursor);
        return response;
    }

    public List<Integer> getPaginatedFeed(String type, Integer userId, int cursor, Pageable pageable) {
        switch (type.toLowerCase()) {
            case "foryou":
                return postRepository.findNextPaginatedPostIds(cursor, pageable);

            case "liked":
                if (userId == null) throw new IllegalArgumentException("userId required for liked feed");
                return likeRepository.findPaginatedLikedPostIds(userId, cursor, pageable);

            default:
                throw new IllegalArgumentException("Unknown feed type: " + type);
        }
    }



}
