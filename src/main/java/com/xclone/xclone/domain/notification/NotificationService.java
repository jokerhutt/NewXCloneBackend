package com.xclone.xclone.domain.notification;

import com.xclone.xclone.domain.post.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final PostRepository postRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, PostRepository postRepository) {
        this.notificationRepository = notificationRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public boolean addNotification(NewNotification newNotification) {

        if (checkExistingNotification(newNotification)) {
            return false;
        }

        Notification notification = new Notification();
        notification.setSenderId(newNotification.senderId);
        notification.setReceiverId(newNotification.receiverId);
        notification.setSeen(false);
        if (newNotification.postId != null) {
            notification.setPostId(newNotification.postId);
        }
        notification.setType(newNotification.type);
        notificationRepository.save(notification);

        if (notificationRepository.existsById(notification.getId())) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkExistingNotification(NewNotification newNotification) {
        if (newNotification.type.equals("follow")) {
            if (notificationRepository.existsBySenderIdAndReceiverIdAndType(newNotification.senderId, newNotification.receiverId, newNotification.type)) {
                return true;
            }
        } else if (notificationRepository.existsBySenderIdAndReceiverIdAndTypeAndPostId(newNotification.senderId, newNotification.receiverId, newNotification.type, newNotification.postId)) {
                return true;
        }

        return false;

    }


}
