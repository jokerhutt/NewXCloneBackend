package com.xclone.xclone.domain.post;

import com.xclone.xclone.domain.bookmark.Bookmark;
import com.xclone.xclone.domain.bookmark.BookmarkRepository;
import com.xclone.xclone.domain.like.Like;
import com.xclone.xclone.domain.like.LikeRepository;
import com.xclone.xclone.domain.notification.NotificationService;
import com.xclone.xclone.domain.retweet.Retweet;
import com.xclone.xclone.domain.retweet.RetweetService;
import com.xclone.xclone.domain.user.User;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final NotificationService notificationService;
    private final RetweetService retweetService;
    private final PostMediaRepository postMediaRepository;

    @Autowired
    public PostService(PostRepository postRepository, LikeRepository likeRepository, BookmarkRepository bookmarkRepository, NotificationService notificationService, RetweetService retweetService, PostMediaRepository postMediaRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.notificationService = notificationService;
        this.retweetService = retweetService;
        this.postMediaRepository = postMediaRepository;
    }

    public PostDTO findPostDTOById(int id) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isPresent()) {
            Post postEntity = post.get();
            return createPostDTO(postEntity);
        } else {
            return null;
        }

    }

    public PostMedia getPostMediaById(int id) {
        PostMedia postMedia = postMediaRepository.findById(id).orElse(null);
        if (postMedia == null) {
            return null;
        } else {
            System.out.println("FILE NAME IS: " + postMedia.getFileName() + "DATA: " + postMedia.getData());
            return postMedia;
        }

    }

    public Map preparePostMediaMapToBase64 (PostMedia postMedia) {

        String base64 = Base64.getEncoder().encodeToString(postMedia.getData());

        Map<String, String> response = new HashMap<>();
        response.put("src", "data:" + postMedia.getMimeType() + ";base64," + base64);
        response.put("alt", postMedia.getFileName());
        response.put("type", postMedia.getMimeType());

        return response;
    }

    public List<PostMedia> getAllPostMediaByPostId(ArrayList<Integer> ids) {
        return postMediaRepository.findAllByPostIdIn(ids);
    }

    public Optional<Post> findByUserId(int id) {
        return postRepository.findByUserId(id);
    }

    public ArrayList<PostDTO> findAllPostDTOByIds( ArrayList<Integer> ids) {
        ArrayList<PostDTO> postDTOs = new ArrayList<>();
        List<Post> posts = postRepository.findAllById(ids);

        for (Post post : posts) {
            postDTOs.add(createPostDTO(post));
        }

        return postDTOs;
    }

    private PostDTO createPostDTO(Post post) {
        ArrayList<Like> likedBy = likeRepository.findAllByLikedPostId(post.getId());
        ArrayList<Integer> likedByIds = new ArrayList<>();

        ArrayList<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkedPost(post.getId());
        ArrayList<Integer> bookmarkIds = new ArrayList<>();

        ArrayList<Post> replies = postRepository.findAllByParentId(post.getId());
        ArrayList<Integer> repliesIds = new ArrayList<>();

        ArrayList<Integer> retweeters = retweetService.getAllRetweetersByPostID(post.getId());
        ArrayList<Integer> postMediaIds = new ArrayList<>();
        ArrayList<PostMedia> postMedia = postMediaRepository.findAllByPostId(post.getId());

        for (Like like : likedBy) {
            likedByIds.add(like.getLikerId());
        }

        for (Bookmark bookmark : bookmarks) {
            bookmarkIds.add(bookmark.getBookmarkedBy());
        }

        for (Post reply: replies) {
            repliesIds.add(reply.getId());
        }

        if (postMedia != null) {
            for (PostMedia postM: postMedia) {
                postMediaIds.add(postM.getId());
            }
        }

        return new PostDTO(post, likedByIds, bookmarkIds, repliesIds, retweeters, postMediaIds);
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

    public Post createPostEntity(Integer userId, String text, Integer parentId) {
        Post post = new Post();
        post.setUserId(userId);
        post.setText(text);
        if (parentId != null) {
            post.setParentId(parentId);
        }

        return postRepository.save(post);
    }

    public void savePostImages(Integer postId, List<MultipartFile> images) {
        for (MultipartFile image : images) {
            try {
                byte[] data = image.getBytes();
                String fileName = image.getOriginalFilename();
                String mimeType = image.getContentType();

                PostMedia media = new PostMedia(postId, fileName, mimeType, data);
                postMediaRepository.save(media);
            } catch (IOException e) {
                throw new RuntimeException("Failed to process image: " + image.getOriginalFilename());
            }
        }
    }

}