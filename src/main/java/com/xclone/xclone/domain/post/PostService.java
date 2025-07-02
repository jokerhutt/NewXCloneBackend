package com.xclone.xclone.domain.post;

import com.xclone.xclone.domain.bookmark.Bookmark;
import com.xclone.xclone.domain.bookmark.BookmarkRepository;
import com.xclone.xclone.domain.feed.EdgeRank;
import com.xclone.xclone.domain.like.Like;
import com.xclone.xclone.domain.like.LikeRepository;
import com.xclone.xclone.domain.notification.NotificationService;
import com.xclone.xclone.domain.retweet.Retweet;
import com.xclone.xclone.domain.retweet.RetweetRepository;
import com.xclone.xclone.storage.CloudStorageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class PostService {

    private final CloudStorageService cloudStorageService;
    @PersistenceContext
    private EntityManager entityManager;

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final NotificationService notificationService;
    private final RetweetRepository retweetRepository;
    private final PostMediaRepository postMediaRepository;
    private final EdgeRank edgeRank;

    @Autowired
    public PostService(PostRepository postRepository, LikeRepository likeRepository, BookmarkRepository bookmarkRepository, NotificationService notificationService, RetweetRepository retweetRepository, PostMediaRepository postMediaRepository, EdgeRank edgeRank, CloudStorageService cloudStorageService) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.notificationService = notificationService;
        this.retweetRepository = retweetRepository;
        this.postMediaRepository = postMediaRepository;
        this.edgeRank = edgeRank;
        this.cloudStorageService = cloudStorageService;
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
//            System.out.println("FILE NAME IS: " + postMedia.getFileName() + "DATA: " + postMedia.getData());
            return postMedia;
        }

    }

    //TODO add some kind of feed refresh intervals?
    //TODO how to handle feed refresh when user refreshing but still authenticated?


    public List<PostMedia> getAllPostMediaByPostId(ArrayList<Integer> ids) {
        return postMediaRepository.findAllByPostIdIn(ids);
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

        ArrayList<Retweet> retweets = retweetRepository.findAllByReferenceId(post.getId());
        ArrayList<Integer> retweeters = new ArrayList<>();
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

        for (Retweet retweet: retweets) {
            retweeters.add(retweet.getRetweeterId());
        }

        return new PostDTO(post, likedByIds, bookmarkIds, repliesIds, retweeters, postMedia);
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

    @Transactional
    public Post createPostEntity(Integer userId, String text, Integer parentId) {
        Post post = new Post();
        post.setUserId(userId);
        post.setText(text);
        if (parentId != null) {
            post.setParentId(parentId);
        }

        System.out.println("Saving post by: " + userId);

        Post newPost = postRepository.save(post);

        return newPost;
    }

    public void savePostImages(int postId, List<MultipartFile> images) throws IOException {
        for (MultipartFile file : images) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String mimeType = file.getContentType();

            System.out.println("MIME: " + file.getContentType() + ", length: " + file.getContentType().length());

            String url = cloudStorageService.upload(fileName, file.getInputStream(), mimeType);

            PostMedia media = new PostMedia(postId, file.getOriginalFilename(), mimeType, url);

            postMediaRepository.save(media);
        }
    }

}