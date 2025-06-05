package com.xclone.xclone.domain.retweet;

import com.xclone.xclone.domain.notification.NotificationService;
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






}
