package com.xclone.xclone.domain.feed;

import com.xclone.xclone.domain.bookmark.BookmarkRepository;
import com.xclone.xclone.domain.like.LikeRepository;
import com.xclone.xclone.domain.post.*;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserRepository;
import com.xclone.xclone.domain.user.UserService;
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
    private final BookmarkRepository bookmarkRepository;
    private final FeedEntryRepository feedEntryRepository;
    private final EdgeRank edgeRank;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostMediaRepository postMediaRepository;
    PostRepository postRepository;

    @Autowired
    public FeedService(PostRepository postRepository, LikeRepository likeRepository, BookmarkRepository bookmarkRepository, FeedEntryRepository feedEntryRepository, EdgeRank edgeRank, UserRepository userRepository, UserService userService, PostMediaRepository postMediaRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.feedEntryRepository = feedEntryRepository;
        this.edgeRank = edgeRank;
        this.userRepository = userRepository;
        this.userService = userService;
        this.postMediaRepository = postMediaRepository;
    }

    public Map<String, Object> getPaginatedPostIds(int cursor, int limit, Integer userId, String type) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Integer> ids = getPaginatedFeed(type, userId, cursor, pageable);

        Integer nextCursor = ids.size() < limit ? null : ids.get(ids.size() - 1);

        Map<String, Object> response = new HashMap<>();
        response.put("posts", ids);
        response.put("nextCursor", nextCursor);
        return response;
    }

    public List<Integer> getPaginatedFeed(String type, Integer userId, int cursor, Pageable pageable) {
        switch (type.toLowerCase()) {
            case "for you":
                return getUsersForYouFeed(userId, cursor, pageable);

            case "following":
                UserDTO user = userService.findUserByID(userId);
                return postRepository.findPaginatedPostIdsFromFollowedUsers(user.following, cursor, pageable);

            case "liked":
                if (userId == null) throw new IllegalArgumentException("userId required for liked feed");
                return likeRepository.findPaginatedLikedPostIds(userId, cursor, pageable);

            case "tweets":
                if (userId == null) throw new IllegalArgumentException("userId required for posts feed");
                return postRepository.findPaginatedTweetIdsByUserId(userId, cursor, pageable);

            case "replies":
                if (userId == null) throw new IllegalArgumentException("userId required for replies feed");
                return postRepository.findPaginatedReplyIdsByUserId(userId, cursor, pageable);

            case "bookmarks":
                if (userId == null) throw new IllegalArgumentException("userId required for bookmarks feed");
                return bookmarkRepository.findPaginatedBookmarkedPostIds(userId, cursor, pageable);

            case "media":
                if (userId == null) throw new IllegalArgumentException("userId required for media feed");
                return postRepository.findPaginatedPostIdsWithMediaByUserId(userId, cursor, pageable);

            default:
                throw new IllegalArgumentException("Unknown feed type: " + type);
        }
    }

    private List<Integer> getUsersForYouFeed (Integer userId, int cursor, Pageable pageable) {

        if (userId == null) {
            System.out.println("userId required for you feed, getting generic");
            return postRepository.findNextPaginatedPostIds(cursor, pageable);
        } else {

            printFeed(feedEntryRepository.findByUserIdOrderByPositionAsc(13), userId);

            List<Integer> ids = feedEntryRepository.getFeedPostIdsCustom(userId, cursor, pageable);
            System.out.println("Old ids is: " + ids);

            if (ids.isEmpty()) {
                ArrayList<PostRank> postRanks = edgeRank.buildFeed(userId);
                edgeRank.saveFeed(userId, postRanks);
                ids = feedEntryRepository.getFeedPostIdsCustom(userId, cursor, pageable);
            }
            System.out.println("to return ids is: " + ids);
            return ids;
        }
    }

    private void printFeed(List<FeedEntry> feedEntries, Integer userId) {

        UserDTO user = userService.findUserByID(userId);

        System.out.println("GENERATED FEED FOR USER: " + user.username);
        System.out.println("--------------------------------------------------------------");
        System.out.printf("| %-7s | %-7s | %-9s | %-9s |\n", "PostId", "UserId", "Score", "Position");
        System.out.println("--------------------------------------------------------------");

        for (FeedEntry entry : feedEntries) {
            System.out.printf(
                    "| %-7d | %-7d | %-9.4f | %-9d |\n",
                    entry.getPostId(),
                    entry.getUserId(),
                    entry.getScore(),
                    entry.getPosition()
            );
        }

        System.out.println("--------------------------------------------------------------");
    }

}

