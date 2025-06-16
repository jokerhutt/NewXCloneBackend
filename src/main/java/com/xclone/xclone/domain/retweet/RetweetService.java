package com.xclone.xclone.domain.retweet;

import com.xclone.xclone.domain.notification.NotificationService;
import com.xclone.xclone.domain.post.PostDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Service
public class RetweetService {

    private final NotificationService notificationService;
    private final RetweetRepository retweetRepository;

    @Autowired
    public RetweetService(NotificationService notificationService, RetweetRepository retweetRepository) {
        this.notificationService = notificationService;
        this.retweetRepository = retweetRepository;
    }

    public ArrayList<Integer> getAllRetweetedPostsByUserID(Integer retweeterId) {

        ArrayList<Retweet> retweets =  retweetRepository.findAllByRetweeterId(retweeterId);
        ArrayList<Integer> referenceIds = new ArrayList<>();
        for (Retweet retweet : retweets) {
            referenceIds.add(retweet.getReferenceId());
        }
        return referenceIds;

    }

    public ArrayList<Integer> getAllRetweetersByPostID(Integer postId) {
        ArrayList<Retweet> retweets =  retweetRepository.findAllByReferenceId(postId);
        ArrayList<Integer> retweeters = new ArrayList<>();
        for (Retweet retweet : retweets) {
            retweeters.add(retweet.getRetweeterId());
        }
        return retweeters;
    }



    @Transactional
    public void createRetweet(NewRetweet newRetweet) {
        if (!retweetRepository.existsByRetweeterIdAndReferenceId(newRetweet.retweeterId, newRetweet.referenceId)) {
            System.out.println("Received create request");

            Retweet retweet = new Retweet();
            retweet.setRetweeterId(newRetweet.retweeterId);
            retweet.setReferenceId(newRetweet.referenceId);
            retweet.setType(newRetweet.type);

            notificationService.createNotificationFromType(newRetweet.retweeterId, newRetweet.referenceId, "repost");

            retweetRepository.save(retweet);
        }
    }

    @Transactional
    public void deleteRetweet(NewRetweet newRetweet) {
        System.out.println("Received delete request");
        Retweet toDelete = retweetRepository.findByRetweeterIdAndReferenceId(newRetweet.retweeterId, newRetweet.referenceId);
        if (toDelete != null) {
            notificationService.deleteNotificationFromType(newRetweet.retweeterId, newRetweet.referenceId, "repost");
            retweetRepository.delete(toDelete);
        }

    }




}
