package com.xclone.xclone.domain.bookmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xclone.xclone.domain.post.PostDTO;
import com.xclone.xclone.helpers.MvcTestHelper;
import com.xclone.xclone.helpers.ServiceLayerHelper;
import com.xclone.xclone.security.JwtService;
import com.xclone.xclone.security.SecurityConfig;
import com.xclone.xclone.utils.TestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookmarkController.class)
@Import(SecurityConfig.class)
class BookmarkControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    BookmarkService bookmarkService;

    static final class NewBookmarkBody {
        public Integer bookmarkedPost;
        NewBookmarkBody(Integer bookmarkedPost) { this.bookmarkedPost = bookmarkedPost; }
    }

    private String bearer(Integer userId) {
        String token = "test-token";
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractUserId(token)).thenReturn(userId);
        return "Bearer " + token;
    }

    @Test
    void createBookmark_returns200_andEchoesServiceDto() throws Exception {
        Integer authenticatedUserId = TestConstants.USER_ID;
        Integer bookmarkedPostId = TestConstants.TWEET_ID;

        PostDTO dto = ServiceLayerHelper.createMockPostDTO();
        when(bookmarkService.addNewBookmark(eq(authenticatedUserId), eq(bookmarkedPostId)))
                .thenReturn(dto);

        String body = MvcTestHelper.toJson(objectMapper, new NewBookmarkBody(bookmarkedPostId));
        String expected = MvcTestHelper.toJson(objectMapper, dto);

        mockMvc.perform(post("/api/bookmarks/create")
                        .header("Authorization", bearer(authenticatedUserId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(expected, false));

        verify(bookmarkService).addNewBookmark(authenticatedUserId, bookmarkedPostId);
    }

    @Test
    void createBookmark_returns409_onIllegalState() throws Exception {
        Integer authenticatedUserId = TestConstants.USER_ID;
        Integer bookmarkedPostId = TestConstants.TWEET_ID;

        when(bookmarkService.addNewBookmark(eq(authenticatedUserId), eq(bookmarkedPostId)))
                .thenThrow(new IllegalStateException("Already bookmarked"));

        String body = MvcTestHelper.toJson(objectMapper, new NewBookmarkBody(bookmarkedPostId));

        mockMvc.perform(post("/api/bookmarks/create")
                        .header("Authorization", bearer(authenticatedUserId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Already bookmarked"));
    }

    @Test
    void deleteBookmark_returns200_withServiceDto() throws Exception {
        Integer authenticatedUserId = TestConstants.USER_ID;
        Integer bookmarkedPostId = TestConstants.TWEET_ID;

        PostDTO dto = ServiceLayerHelper.createMockPostDTO();

        when(bookmarkService.deleteBookmark(eq(authenticatedUserId), eq(bookmarkedPostId)))
                .thenReturn(dto);

        String body = MvcTestHelper.toJson(objectMapper, new NewBookmarkBody(bookmarkedPostId));
        String expected = MvcTestHelper.toJson(objectMapper, dto);

        mockMvc.perform(post("/api/bookmarks/delete")
                        .header("Authorization", bearer(authenticatedUserId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().json(expected, false));

        verify(bookmarkService).deleteBookmark(authenticatedUserId, bookmarkedPostId);

    }
}