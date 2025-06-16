package com.xclone.xclone.domain.follow;

import com.xclone.xclone.domain.notification.NotificationService;
import com.xclone.xclone.domain.user.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FollowService {

    private final NotificationService notificationService;
    private FollowRepository followRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, NotificationService notificationService) {
        this.followRepository = followRepository;
        this.notificationService = notificationService;
    }

    public ArrayList<Integer> getAllUserFollowing (Integer userId) {

        ArrayList<Integer> followingUserIds = new ArrayList<>();
        ArrayList<Follow> follows = followRepository.findAllByFollowerId(userId);
        for (Follow follow : follows) {
            followingUserIds.add(follow.getFollowedId());
        }
        return followingUserIds;

    }

    public ArrayList<Integer> getAllUserFollowers (Integer userId) {

        ArrayList<Integer> followerUserIds = new ArrayList<>();
        ArrayList<Follow> follows = followRepository.findAllByFollowedId((userId));
        for (Follow follow : follows) {
            followerUserIds.add(follow.getFollowerId());
        }
        return followerUserIds;

    }

    @Transactional
    public Integer addNewFollow (Integer followerId, Integer followedId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowedId(followedId);
        System.out.println("Trying to ass follow " + followerId + " followed " + followedId);

        if (!followRepository.existsByFollowedIdAndFollowerId(followedId, followerId)) {
            System.out.println("Adding follow " + followedId);
            followRepository.save(follow);
            notificationService.createNotificationFromType(followerId, followedId, "follow");
        } else {
            System.out.println("Follow already exists " + followedId);
        }

        return followedId;

    }

    @Transactional
    public Integer deleteFollow(Integer followerId, Integer followedId) {
        Optional<Follow> toDeleteFollow = followRepository.findByFollowedIdAndFollowerId(followedId, followerId);
        System.out.println("Trying to delete follow " + followerId + " followed " + followedId);
        if (toDeleteFollow.isPresent()) {
            System.out.println("Deleting follow " + followedId);
            followRepository.delete(toDeleteFollow.get());
            notificationService.deleteNotificationFromType(followerId, followedId, "follow");
        } else {
            System.out.println("Not found follow");
        }

        return followedId;

    }



}
