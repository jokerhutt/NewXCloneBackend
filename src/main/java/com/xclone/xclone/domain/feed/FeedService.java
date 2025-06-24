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

import java.sql.Timestamp;
import java.util.*;

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

    public Map<String, Object> getPaginatedPostIds(long cursor, int limit, Integer userId, String type) {
        Pageable pageable = PageRequest.of(0, limit);

        List<Integer> ids;

        if (type.equals("Tweets")) {
            Timestamp cursorTimestamp = new Timestamp(cursor);
            if (userId == null) throw new IllegalArgumentException("userId required for posts feed");
            ids = postRepository.findPaginatedTweetIdsByUserIdByTime(userId, cursorTimestamp, pageable);
        } else {
            ids = getPaginatedFeed(type, userId, cursor, pageable);
            System.out.println("Got paginated post ids: " + ids + " size: " + ids.size() + " cursor: " + cursor + " limit: " + limit + " user: " + userId + " type: " + type);
        }

        Long lastPostId = null;

        if (ids.size() == 0) {
            return null;
        } else if (type.equals("For You") && userId != null) {
            if (ids.size() > 0) {
                Integer lastPostIdInt = ids.get(ids.size() - 1);
                FeedEntry feedEntry = feedEntryRepository.findByPostIdAndUserId(lastPostIdInt, userId);
                lastPostId = Long.valueOf(feedEntry.getPosition());
            }
        } else if (type.equals("Tweets") && userId != null) {
            Integer lastPostIdInt = ids.size() < limit ? null : ids.get(ids.size() - 1);
            if (lastPostIdInt != null) {
                Optional<Post> post = postRepository.findById(lastPostIdInt);
                if (post.isPresent()) {
                    lastPostId = post.get().getCreatedAt().getTime();
                } else {
                    throw new IllegalArgumentException("PostId doesn't exist");
                }
            } else {
                lastPostId = null;
            }

        } else {
            lastPostId = Long.valueOf(ids.size() < limit ? null : ids.get(ids.size() - 1));

        }

        Map<String, Object> response = new HashMap<>();
        response.put("posts", ids);
        response.put("nextCursor", lastPostId);
        System.out.println("Returned: " + Arrays.toString(ids.toArray()) + " cursor: " + lastPostId + " limit: " + limit + " user: " + userId + " type: " + type);
        return response;
    }

    public List<Integer> getPaginatedFeed(String type, Integer userId, long cursor, Pageable pageable) {
        switch (type.toLowerCase()) {
            case "for you":
                return getUsersForYouFeed(userId, cursor, pageable);

            case "following":
                UserDTO user = userService.findUserByID(userId);
                return postRepository.findPaginatedPostIdsFromFollowedUsers(user.following, cursor, pageable);

            case "liked":
                if (userId == null) throw new IllegalArgumentException("userId required for liked feed");
                return likeRepository.findPaginatedLikedPostIds(userId, cursor, pageable);

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

    private List<Integer> getUsersForYouFeed (Integer userId, long cursor, Pageable pageable) {
//        printFeed(feedEntryRepository.findAllByUserId(13), 13);

        if (userId == null) {
            System.out.println("userId required for you feed, getting generic");
            return postRepository.findNextPaginatedPostIds(cursor, pageable);
        } else {

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

