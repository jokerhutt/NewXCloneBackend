package com.xclone.xclone.domain.user;

import com.xclone.xclone.domain.bookmark.BookmarkService;
import com.xclone.xclone.domain.feed.EdgeRank;
import com.xclone.xclone.domain.feed.PostRank;
import com.xclone.xclone.domain.follow.Follow;
import com.xclone.xclone.domain.follow.FollowRepository;
import com.xclone.xclone.domain.follow.FollowService;
import com.xclone.xclone.domain.like.LikeService;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.post.PostDTO;
import com.xclone.xclone.domain.post.PostService;
import com.xclone.xclone.domain.retweet.RetweetService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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



    public UserDTO findUserByID(Integer id) {

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

            if (type.equals("profilePic")) {
                String profilePic = Base64.getEncoder().encodeToString(user.get().getProfilePicture());
                return profilePic;
            }

            if (type.equals("bannerImage")) {
                String bannerImage = Base64.getEncoder().encodeToString(user.get().getBannerImage());
                return bannerImage;
            }

        }

        return null;

    }

    public Map<String, Object> getPaginatedTopUsers(Long cursor, int limit) {
        List<Integer> userIds = userRepository.findUserIdsByFollowerCount(cursor, limit);

        System.out.println("Got paginated user ids: " + userIds + " size: " + userIds.size());

        Long nextCursor = null;

        if (!userIds.isEmpty()) {
            Integer lastUserId = userIds.get(userIds.size() - 1);
            UserDTO lastUser = findUserByID(lastUserId);
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


//        this.profilePicture = Base64.getEncoder().encodeToString(user.getProfilePicture());
//        this.bannerImage = Base64.getEncoder().encodeToString(user.getBannerImage());

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

    public User findByUserName (String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    private void checkIfUserExistsOnSignup(String email, String username) {
        if (userRepository.existsUserByEmail(email)) {
            throw new IllegalStateException("Email already in use");
        }
        if (userRepository.existsUserByUsername(username)) {
            throw new IllegalStateException("Username already in use");
        }
    }

    @Transactional
    public UserDTO registerUser (User signupUser) {

        checkIfUserExistsOnSignup(signupUser.getEmail(), signupUser.getUsername());

        User user = new User();
        user.setUsername(signupUser.getUsername());
        user.setPassword(signupUser.getPassword()); // consider hashing!
        user.setEmail(signupUser.getEmail());
        user.setBio(signupUser.getBio());
        user.setDisplayName(signupUser.getDisplayName());

        user.setCreatedAt(Timestamp.from(Instant.now()));
        System.out.println("ProfilePicture length: " + signupUser.getProfilePicture().length);
        System.out.println("BannerImage length: " + signupUser.getBannerImage().length);
        System.out.println("BannerImage preview: " + Base64.getEncoder().encodeToString(signupUser.getBannerImage()).substring(0, 100));

        user.setProfilePicture(signupUser.getProfilePicture());
        user.setBannerImage(signupUser.getBannerImage());


        userRepository.save(user);
        edgeRank.generateFeed(user.getId());
        System.out.println("Saved user ID: " + user.getId());
        return this.findUserByID(user.getId());

    }

    @Transactional
    public ResponseEntity<?> verifyUser (LoginRequest loginRequest) {

        User user = this.findByUserName(loginRequest.username);
        if (user != null) {
            if (loginRequest.password.equals(user.getPassword())) {
                System.out.println("Password verified, generating feed");
                edgeRank.generateFeed(user.getId());
                System.out.println("Generated feed");

                UserDTO dto = this.findUserByID(user.getId());
                return ResponseEntity.ok(dto);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Incorrect password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User does not exist");
        }


    }

    @Transactional
    void generateFeed(Integer userId) {
        edgeRank.generateFeed(userId);
    }



}
