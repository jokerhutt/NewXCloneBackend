package com.xclone.xclone.domain.user;

import com.xclone.xclone.domain.bookmark.BookmarkService;
import com.xclone.xclone.domain.feed.EdgeRank;
import com.xclone.xclone.domain.follow.Follow;
import com.xclone.xclone.domain.follow.FollowRepository;
import com.xclone.xclone.domain.like.LikeService;
import com.xclone.xclone.domain.post.PostService;
import com.xclone.xclone.domain.retweet.RetweetService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.xclone.xclone.util.MediaParsingUtils.encodeUserMediaToBase64;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostService postService;
    private final BookmarkService bookmarkService;
    private final LikeService likeService;

    private final RetweetService retweetService;
    private final FollowRepository followRepository;
    private final EdgeRank edgeRank;

    @Autowired
    public UserService(UserRepository userRepository, PostService postService, BookmarkService bookmarkService, LikeService likeService, RetweetService retweetService, FollowRepository followRepository, EdgeRank edgeRank) {
        this.userRepository = userRepository;
        this.postService = postService;
        this.bookmarkService = bookmarkService;
        this.likeService = likeService;

        this.retweetService = retweetService;
        this.followRepository = followRepository;
        this.edgeRank = edgeRank;
    }

    private UserDTO createUserDTO(User user) {
        ArrayList<Integer> userPosts = postService.findAllPostsByUserId(user.getId());
        ArrayList<Integer> userBookmarks = bookmarkService.getAllUserBookmarks(user.getId());
        ArrayList<Integer> userLikes = likeService.getAllUserLikes(user.getId());
        ArrayList<Follow> userFollowing = followRepository.findAllByFollowerId(user.getId());
        ArrayList<Integer> userFollowingIds = new ArrayList<>();
        for (Follow follow : userFollowing) {
            userFollowingIds.add(follow.getFollowedId());
        }
        ArrayList<Integer> userFollowerIds = new ArrayList<>();
        ArrayList<Follow> userFollowers = followRepository.findAllByFollowedId(user.getId());
        for (Follow follow : userFollowers) {
            userFollowerIds.add(follow.getFollowerId());
        }
        ArrayList<Integer> userReplies = postService.findAllRepliesByUserId(user.getId());
        ArrayList<Integer> userRetweets = retweetService.getAllRetweetedPostsByUserID(user.getId());
        return new UserDTO(user, userPosts, userBookmarks, userLikes, userFollowerIds, userFollowingIds, userReplies, userRetweets);
    }



    public UserDTO generateUserDTOByUserId(Integer id) {
        Optional<User> user = this.findById(id);
        if (user.isPresent()) {
            return createUserDTO(user.get());
        } else {
            return null;
        }
    }

    public String getUserProfileMedia (Integer userId, String type) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
           return encodeUserMediaToBase64(user.get(), type);
        }
        return null;
    }

    public Map<String, Object> getPaginatedTopUsers(Long cursor, int limit) {
        List<Integer> userIds = userRepository.findUserIdsByFollowerCount(cursor, limit);

        System.out.println("Got paginated user ids: " + userIds + " size: " + userIds.size());

        Long nextCursor = null;

        if (!userIds.isEmpty()) {
            Integer lastUserId = userIds.get(userIds.size() - 1);
            UserDTO lastUser = generateUserDTOByUserId(lastUserId);
            nextCursor = (long) lastUser.followers.size();
        }

        System.out.println("nextCursor: " + nextCursor);

        Map<String, Object> response = new HashMap<>();
        response.put("users", userIds);
        response.put("nextCursor", nextCursor);

        return response;
    }

    public List<Integer> searchUsersByName(String query) {
        List<User> userList = userRepository.searchByUsernameOrDisplayName(query);
        ArrayList<Integer> userIds = new ArrayList<>();
        for (User user : userList) {
            userIds.add(user.getId());
        }
        return userIds;
    }

    public ArrayList<UserDTO> findAllUserDTOByIds( ArrayList<Integer> ids) {
        ArrayList<UserDTO> userDTOs = new ArrayList<>();
        userRepository.findAllById(ids).forEach(user -> {
            userDTOs.add(createUserDTO(user));
        });
        return userDTOs;
    }

    public Optional<User> findById (Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    void generateFeed(Integer userId) {
        edgeRank.generateFeed(userId);
    }



}
