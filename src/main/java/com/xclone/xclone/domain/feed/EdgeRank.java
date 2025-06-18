package com.xclone.xclone.domain.feed;

import com.xclone.xclone.domain.like.Like;
import com.xclone.xclone.domain.like.LikeRepository;
import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.post.PostMediaRepository;
import com.xclone.xclone.domain.post.PostRepository;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EdgeRank {

    private final PostRepository postRepository;
    private final PostMediaRepository postMediaRepository;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final FeedEntryRepository feedEntryRepository;

    @Autowired
    public EdgeRank (PostRepository postRepository, PostMediaRepository postMediaRepository, LikeRepository likeRepository, UserService userService, FeedEntryRepository feedEntryRepository) {
        this.postRepository = postRepository;
        this.postMediaRepository = postMediaRepository;
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.feedEntryRepository = feedEntryRepository;
    }

    public ArrayList<PostRank> buildFeed (Integer userId) {

        ArrayList<Integer> feed = new ArrayList<>();
        UserDTO userDTO = userService.findUserByID(userId);

        List<Post> posts = postRepository.findAllTopLevelPosts();
        ArrayList<PostRank> postRanks = new ArrayList<>();

        for (Post post : posts) {
            postRanks.add(new PostRank(post));
        }

        computeTotalScore(postRanks, userDTO);


        postRanks.sort((a, b) -> Double.compare(b.totalScore, a.totalScore));
        return postRanks;


    }

    @Transactional
    public void saveFeed(Integer userId, ArrayList<PostRank> feed) {
        feedEntryRepository.deleteByUserId(userId);

        ArrayList<FeedEntry> feedEntries = new ArrayList<>();
        for (int i = 0; i < feed.size(); i++) {
            PostRank pr = feed.get(i);
            FeedEntry feedEntry = new FeedEntry();
            feedEntry.setUserId(userId);
            feedEntry.setPostId(pr.post.getId());
            feedEntry.setScore(pr.totalScore);
            feedEntry.setPosition(i);
            feedEntries.add(feedEntry);
        }

        feedEntryRepository.saveAll(feedEntries);
    }


    private void computeTotalScore (ArrayList<PostRank> postranks, UserDTO feedUser) {
        for (PostRank postRank : postranks) {
            computeAffinity(postRank, feedUser);
            computeWeights(postRank);
            computeTimeDecayValue(postRank);
            postRank.computeTotalScore();
        }
    }

    private void computeTimeDecayValue (PostRank postRank) {
        postRank.timeDecay += computeTimeDecay(postRank.post);
    }

    private void computeAffinity (PostRank postToRank, UserDTO feedUser) {

        List<Integer> postIdsByOther = postRepository.findPostIdsByAuthor(postToRank.post.getUserId());
        Set<Integer> postIdsByOtherSet = new HashSet<>(postIdsByOther);

        postToRank.affinity += computeFollowingAffinity(feedUser, postToRank.post.getUserId());
        postToRank.affinity += computeHasLikedAffinity(feedUser, postIdsByOtherSet);
        postToRank.affinity += computeHasRepliedAffinity(feedUser, postIdsByOtherSet);

    }

    private void computeWeights (PostRank postToRank) {

        postToRank.weight += computeHasMediaAffinity(postToRank);
        postToRank.weight += computeLikeWeights(postToRank);

    }

    private float computeHasMediaAffinity (PostRank postToRank) {
        if (postMediaRepository.findAllByPostId(postToRank.post.getId()).isEmpty()) {
            return 0;
        } else {
            return 0.4f;
        }
    }

    private float computeLikeWeights (PostRank postToRank) {
        ArrayList <Like> likes = likeRepository.findAllByLikedPostId(postToRank.post.getId());
        return (float) Math.log(likes.size() + 1);
    }

    private double computeTimeDecay(Post post) {
        LocalDateTime createdAt = post.getCreatedAt().toLocalDateTime();
        long hoursSince = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        return 1.0 / (hoursSince + 1);
    }

    private float computeFollowingAffinity (UserDTO feedUser, Integer postOwnerId) {
        if (feedUser.following.contains(postOwnerId)) {
            return 2;
        } else {
            return 1f;
        }
    }

    private float computeHasLikedAffinity (UserDTO feedUser, Set<Integer> postIdsByOtherSet) {
        if (feedUser.likedPosts.stream().anyMatch(postIdsByOtherSet::contains)) {
            return 0.5f;
        } else {
            return 0f;
        }
    }

    private float computeHasRepliedAffinity (UserDTO feedUser, Set<Integer> postIdsByOtherSet) {
        if (feedUser.replies.stream().anyMatch(postIdsByOtherSet::contains)) {
            return 0.5f;
        } else {
            return 0f;
        }
    }




}
