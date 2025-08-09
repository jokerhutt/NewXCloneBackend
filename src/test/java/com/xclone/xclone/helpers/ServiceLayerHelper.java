package com.xclone.xclone.helpers;

import com.xclone.xclone.domain.bookmark.Bookmark;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.post.PostDTO;
import com.xclone.xclone.domain.user.User;
import com.xclone.xclone.utils.TestConstants;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceLayerHelper {

    public static User createMockUser() {
        User user = new User();
        user.setId(TestConstants.USER_ID);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setDisplayName("Test User");
        return user;
    }

    public static Post createMockPost(Integer postId, Integer userId) {
        Post post = new Post();
        post.setId(postId);
        post.setUserId(userId);
        post.setText("This is a test post");
        post.setParentId(null);
        return post;
    }

    public static ArrayList<Bookmark> createMockBookmarkList() {
        User user = createMockUser();
        Post post1 = createMockPost(1, user.getId());
        Post post2 = createMockPost(2, user.getId());

        Bookmark bookmark1 = new Bookmark();
        bookmark1.setId(1);
        bookmark1.setBookmarkedBy(user.getId());
        bookmark1.setBookmarkedPost(post1.getId());
        bookmark1.setCreatedAt(Timestamp.from(Instant.now()));

        Bookmark bookmark2 = new Bookmark();
        bookmark2.setId(2);
        bookmark2.setBookmarkedBy(user.getId());
        bookmark2.setBookmarkedPost(post2.getId());
        bookmark2.setCreatedAt(Timestamp.from(Instant.now()));

        return new ArrayList<>(Arrays.asList(bookmark1, bookmark2));
    }

    public static PostDTO createMockPostDTO() {
        Post post = createMockPost(TestConstants.TWEET_ID, TestConstants.USER_ID);
        return new PostDTO(
                post,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                null,
                null
        );
    }

    public static PostDTO createMockPostDTOWithBookmarks(Integer... ids) {
        PostDTO dto = createMockPostDTO();
        dto.bookmarkedBy.addAll(Arrays.asList(ids));
        return dto;
    }



}