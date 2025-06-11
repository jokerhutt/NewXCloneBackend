package com.xclone.xclone.domain.post;

import com.xclone.xclone.domain.bookmark.Bookmark;
import com.xclone.xclone.domain.bookmark.BookmarkRepository;
import com.xclone.xclone.domain.like.Like;
import com.xclone.xclone.domain.like.LikeRepository;
import com.xclone.xclone.domain.notification.NotificationService;
import com.xclone.xclone.domain.user.User;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final NotificationService notificationService;

    @Autowired
    public PostService(PostRepository postRepository, LikeRepository likeRepository, BookmarkRepository bookmarkRepository, NotificationService notificationService) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.notificationService = notificationService;
    }

    public PostDTO findPostDTOById(int id) {
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Like> likedBy = likeRepository.findAllByLikedPostId(post.get().getId());
        ArrayList<Integer> likedByIds = new ArrayList<>();

        ArrayList<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkedPost(post.get().getId());
        ArrayList<Integer> bookmarkIds = new ArrayList<>();
        ArrayList<Post> replies = postRepository.findAllByParentId(id);
        ArrayList<Integer> repliesIds = new ArrayList<>();

        for (Like like : likedBy) {
            likedByIds.add(like.getLikerId());
        }

        for (Bookmark bookmark : bookmarks) {
            bookmarkIds.add(bookmark.getId());
        }

        for (Post reply: replies) {
            repliesIds.add(reply.getId());
        }

        if (post.isPresent()) {
            return new PostDTO(post.get(), likedByIds, bookmarkIds, repliesIds);
        } else {
            return null;
        }
    }

    public Optional<Post> findByUserId(int id) {
        return postRepository.findByUserId(id);
    }

    public ArrayList<PostDTO> findAllPostDTOByIds( ArrayList<Integer> ids) {
        ArrayList<PostDTO> postDTOs = new ArrayList<>();
        List<Post> posts = postRepository.findAllById(ids);

        for (Post post : posts) {
            ArrayList<Like> likedBy = likeRepository.findAllByLikedPostId(post.getId());
            ArrayList<Integer> likedByIds = new ArrayList<>();

            ArrayList<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkedPost(post.getId());
            ArrayList<Integer> bookmarkIds = new ArrayList<>();

            ArrayList<Post> replies = postRepository.findAllByParentId(post.getId());
            ArrayList<Integer> repliesIds = new ArrayList<>();

            for (Like like : likedBy) {
                likedByIds.add(like.getLikerId());
            }

            for (Bookmark bookmark : bookmarks) {
                bookmarkIds.add(bookmark.getId());
            }

            for (Post reply: replies) {
                repliesIds.add(reply.getId());
            }

            postDTOs.add(new PostDTO(post, likedByIds, bookmarkIds, repliesIds));
        }

        return postDTOs;
    }

    public ArrayList<Integer> findAllPostsByUserId(int id) {
        Optional<List<Post>> posts = postRepository.findAllByUserId(id);
        ArrayList<Integer> ids = new ArrayList<>();

        if (posts.isPresent()) {
            for (Post post : posts.get()) {
                if (post.getParentId() == null) {
                    ids.add(post.getId());
                }
            }
        }

        return ids;
    }

    public ArrayList<Integer> findAllPostIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        postRepository.findAll().forEach(post -> {
            if (post.getParentId() == null) {
                ids.add(post.getId());
            }
        });
        return ids;
    }

    public ArrayList<Integer> findAllRepliesByUserId(int id) {
        Optional<List<Post>> posts = postRepository.findAllByUserId(id);
        ArrayList<Integer> ids = new ArrayList<>();
        if (posts.isPresent()) {
            for (Post post : posts.get()) {
                if (post.getParentId() != null) {
                    ids.add(post.getId());
                }
            }
        }

        return ids;
    }

    public ArrayList<Integer> findAllPostsAndRepliesIds () {
        ArrayList<Integer> ids = new ArrayList<>();
        postRepository.findAll().forEach(post -> ids.add(post.getId()));
        return ids;
    }

    @Transactional
    public PostDTO createNewPost(NewPost newPost) {

        Post post = new Post();
        post.setUserId(newPost.userId);
        post.setText(newPost.text);
        if (newPost.parentId != null) {
            post.setParentId(newPost.parentId);
        }

        postRepository.save(post);

        if (post.getParentId() != null) {
            notificationService.handlePostCreateNotification(post.getUserId(), post.getId(), "reply");
        }

        return this.findPostDTOById(post.getId());

    }

}