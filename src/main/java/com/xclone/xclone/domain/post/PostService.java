package com.xclone.xclone.domain.post;

import com.xclone.xclone.domain.bookmark.Bookmark;
import com.xclone.xclone.domain.bookmark.BookmarkRepository;
import com.xclone.xclone.domain.like.Like;
import com.xclone.xclone.domain.like.LikeRepository;
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

    @Autowired
    public PostService(PostRepository postRepository, LikeRepository likeRepository, BookmarkRepository bookmarkRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    public PostDTO findPostDTOById(int id) {
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Like> likedBy = likeRepository.findAllByLikedPostId(post.get().getId());
        ArrayList<Integer> likedByIds = new ArrayList<>();

        ArrayList<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkedPost(post.get().getId());
        ArrayList<Integer> bookmarkIds = new ArrayList<>();

        for (Like like : likedBy) {
            likedByIds.add(like.getLikerId());
        }

        for (Bookmark bookmark : bookmarks) {
            bookmarkIds.add(bookmark.getId());
        }

        if (post.isPresent()) {
            return new PostDTO(post.get(), likedByIds, bookmarkIds);
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

            for (Like like : likedBy) {
                likedByIds.add(like.getLikerId());
            }

            for (Bookmark bookmark : bookmarks) {
                bookmarkIds.add(bookmark.getId());
            }

            postDTOs.add(new PostDTO(post, likedByIds, bookmarkIds));
        }

        return postDTOs;
    }

    public ArrayList<Integer> findAllPostsByUserId(int id) {
        Optional<List<Post>> posts = postRepository.findAllByUserId(id);
        ArrayList<Integer> ids = new ArrayList<>();
        if (posts.isPresent()) {
            for (Post post : posts.get()) {
                ids.add(post.getId());
            }
        }

        return ids;
    }

    public ArrayList<Integer> findAllPostIds () {
        ArrayList<Integer> ids = new ArrayList<>();
        postRepository.findAll().forEach(post -> ids.add(post.getId()));
        return ids;
    }

    @Transactional
    public PostDTO createNewPost(NewPost newPost) {

        Post post = new Post();
        post.setUserId(newPost.userId);
        post.setText(newPost.text);

        postRepository.save(post);

        return this.findPostDTOById(post.getId());

    }

}