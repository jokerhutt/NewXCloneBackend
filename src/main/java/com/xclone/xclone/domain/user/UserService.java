package com.xclone.xclone.domain.user;

import com.xclone.xclone.domain.bookmark.BookmarkService;
import com.xclone.xclone.domain.follow.FollowService;
import com.xclone.xclone.domain.like.LikeService;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.post.PostDTO;
import com.xclone.xclone.domain.post.PostService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostService postService;
    private final BookmarkService bookmarkService;
    private final LikeService likeService;
    private final FollowService followService;

    @Autowired
    public UserService(UserRepository userRepository, PostService postService, BookmarkService bookmarkService, LikeService likeService, FollowService followService) {
        this.userRepository = userRepository;
        this.postService = postService;
        this.bookmarkService = bookmarkService;
        this.likeService = likeService;
        this.followService = followService;
    }



    public UserDTO findUserByID(Integer id) {

        Optional<User> user = this.findById(id);
        ArrayList<Integer> userPosts = postService.findAllPostsByUserId(id);
        ArrayList<Integer> userReplies = postService.findAllRepliesByUserId(id);
        ArrayList<Integer> userBookmarks = bookmarkService.getAllUserBookmarks(id);
        ArrayList<Integer> userLikes = likeService.getAllUserLikes(id);
        ArrayList<Integer> userFollowing = followService.getAllUserFollowing(id);
        ArrayList<Integer> userFollowers = followService.getAllUserFollowers(id);
        if (user.isPresent()) {
            return new UserDTO(user.get(), userPosts, userBookmarks, userLikes, userFollowers, userFollowing, userReplies);
        } else {
            return null;
        }



    }

    public ArrayList<UserDTO> findAllUserDTOByIds( ArrayList<Integer> ids) {
        ArrayList<UserDTO> userDTOs = new ArrayList<>();
        userRepository.findAllById(ids).forEach(user -> {
            ArrayList<Integer> userPosts = postService.findAllPostsByUserId(user.getId());
            ArrayList<Integer> userBookmarks = bookmarkService.getAllUserBookmarks(user.getId());
            ArrayList<Integer> userLikes = likeService.getAllUserLikes(user.getId());
            ArrayList<Integer> userFollowing = followService.getAllUserFollowing(user.getId());
            ArrayList<Integer> userFollowers = followService.getAllUserFollowers(user.getId());
            ArrayList<Integer> userReplies = postService.findAllRepliesByUserId(user.getId());
            userDTOs.add(new UserDTO(user, userPosts, userBookmarks, userLikes, userFollowers, userFollowing, userReplies));
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

    @Transactional
    public UserDTO registerUser (User signupUser) {

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

        user.setProfilePicture(signupUser.getProfilePicture()); // already byte[]
        user.setBannerImage(signupUser.getBannerImage());       // already byte[]


        userRepository.save(user);
        System.out.println("Saved user ID: " + user.getId());
        return this.findUserByID(user.getId());

    }

    public ResponseEntity<?> verifyUser (LoginRequest loginRequest) {

        User user = this.findByUserName(loginRequest.username);
        if (user != null) {
            if (loginRequest.password.equals(user.getPassword())) {
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

}
