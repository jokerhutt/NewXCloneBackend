package com.xclone.xclone.domain.retweet;

import com.xclone.xclone.domain.notification.NotificationService;
import com.xclone.xclone.domain.post.PostDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ArrayList<Retweet> getAllRetweetsByUserID(Integer retweeterId) {

        return retweetRepository.findAllByRetweeterId(retweeterId);

    }

    @Transactional
    public void createRetweet(NewRetweet newRetweet) {
        if (!retweetRepository.existsByRetweeterIdAndReferenceId(newRetweet.retweeterId, newRetweet.referenceId)) {
            Retweet retweet = new Retweet();
            retweet.setRetweeterId(newRetweet.retweeterId);
            retweet.setReferenceId(newRetweet.referenceId);
            retweet.setType(newRetweet.type);
            retweetRepository.save(retweet);
        }
    }

    @Transactional
    public void deleteRetweet(NewRetweet newRetweet) {
        Retweet toDelete = retweetRepository.findByRetweeterIdAndReferenceId(newRetweet.retweeterId, newRetweet.referenceId);
        if (toDelete != null) {
            retweetRepository.delete(toDelete);
        }

    }




}
