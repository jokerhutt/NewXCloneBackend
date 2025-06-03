package com.xclone.xclone.domain.follow;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FollowService {

    private FollowRepository followRepository;

    @Autowired
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public ArrayList<Integer> getAllUserFollowing (Integer userId) {

        ArrayList<Integer> followingUserIds = new ArrayList<>();
        ArrayList<Follow> follows = followRepository.findAllByFollowerId(userId);
        for (Follow follow : follows) {
            followingUserIds.add(follow.getFollowerId());
        }
        return followingUserIds;

    }

    @Transactional
    public boolean addNewFollow (Integer followerId, Integer followedId) {
        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowedId(followedId);

        if (followRepository.existsByFollowedIdAndFollowerId(followedId, followerId)) {
            return false;
        } else {
            followRepository.save(follow);
            return true;
        }

    }

    @Transactional
    public boolean deleteFollow(Integer followerId, Integer followedId) {
        Optional<Follow> toDeleteFollow = followRepository.findByFollowedIdAndFollowerId(followedId, followerId);
        if (toDeleteFollow.isPresent()) {
            followRepository.delete(toDeleteFollow.get());
            return true;
        } else {
            return false;
        }
    }



}
