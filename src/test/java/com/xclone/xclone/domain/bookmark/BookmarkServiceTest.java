package com.xclone.xclone.domain.bookmark;

import com.xclone.xclone.AbstractServiceTest;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.post.PostDTO;
import com.xclone.xclone.domain.user.User;
import com.xclone.xclone.helpers.ServiceLayerHelper;
import com.xclone.xclone.utils.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class BookmarkServiceTest extends AbstractServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    private static Post post;
    private static User authUser;

    @BeforeEach
    public void setup() {

        super.setUp();
        authUser = new User();
        authUser.setId(TestConstants.USER_ID);
        when(userRepository.findById(TestConstants.USER_ID)).thenReturn(Optional.of(authUser));

        post = new Post();
        post.setId(TestConstants.TWEET_ID);
        post.setUserId(authUser.getId());
        post.setText(TestConstants.TWEET_TEXT);

    }

    @Test
    public void GetAllUserBookmarkedPosts() {
        ArrayList<Bookmark> mockBookmarks = ServiceLayerHelper.createMockBookmarkList();

        when(bookmarkRepository.findAllByBookmarkedBy(TestConstants.USER_ID)).thenReturn(mockBookmarks);

        ArrayList<Integer> result = bookmarkService.getAllUserBookmarks(TestConstants.USER_ID);

        assertEquals(2, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));

    }



}
