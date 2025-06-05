package com.xclone.xclone.domain.notification;

import com.xclone.xclone.domain.post.Post;
import com.xclone.xclone.domain.post.PostRepository;
import com.xclone.xclone.domain.user.User;
import com.xclone.xclone.domain.user.UserDTO;
import com.xclone.xclone.domain.user.UserRepository;
import com.xclone.xclone.domain.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class NotificationService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, PostRepository postRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean addNotification(NewNotification newNotification) {

        if (checkExistingNotification(newNotification)) {
            return false;
        }

        if (newNotification.senderId == newNotification.receiverId) {
            return false;
        }

        Notification notification = new Notification();
        notification.setSenderId(newNotification.senderId);
        notification.setReceiverId(newNotification.receiverId);
        notification.setSeen(false);
        notification.setText(newNotification.text);
        notification.setReferenceId(newNotification.referenceId);
        notification.setType(newNotification.type);
        notificationRepository.save(notification);

        if (notificationRepository.existsById(notification.getId())) {
            return true;
        } else {
            return false;
        }

    }

    @Transactional
    public boolean deleteNotification(Notification toDelete) {
        if (notificationRepository.existsById(toDelete.getId())) {
            notificationRepository.deleteById(toDelete.getId());
        }
        return true;
    }



    public void handlePostCreateNotification (Integer senderId, Integer postId, String type) {
        NewNotification toCreate = createNewNotificationTemplateFromPost(senderId, postId, type);

        addNotification(toCreate);
    }

    public void handlePostDeleteNotification (Integer senderId, Integer postId, String type) {
        Notification notification = getNotificationFromSenderAndPost(senderId, postId, type);
        if (notification != null) {
            deleteNotification(notification);
        }
    }

    public void addFollowNotification(Integer followerId, Integer followingId, String type) {
        NewNotification newNotification = createFollowNotificationTemplate(followerId, followingId, type);
        addNotification(newNotification);
    }

    public void removeFollowNotification(Integer followerId, Integer followingId, String type) {
        Notification notification = getFollowNotification(followerId, followingId, type);
        if (notification != null) {
            deleteNotification(notification);
        }
    }

    public NewNotification createNewNotificationTemplateFromPost(Integer senderId, Integer postId, String type) {
        NewNotification newNotification = new NewNotification();
        newNotification.senderId = senderId;
        newNotification.type = type;
        Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) {
            return null;
        } else {
            newNotification.text = post.get().getText();
            newNotification.receiverId = post.get().getUserId();
            newNotification.referenceId = post.get().getId();
        }
        return newNotification;
    }

    public Notification getNotificationFromSenderAndPost(Integer senderId, Integer postId, String type) {
        NewNotification newNotification = new NewNotification();
        newNotification.senderId = senderId;
        newNotification.type = type;
        Optional<Post> post = postRepository.findById(postId);
        if (!post.isPresent()) {
            return null;
        }

        newNotification.text = post.get().getText();
        newNotification.receiverId = post.get().getUserId();
        newNotification.referenceId = post.get().getId();

        Notification notification = notificationRepository.findBySenderIdAndReceiverIdAndTypeAndReferenceIdAndText(
                newNotification.senderId,
                newNotification.receiverId,
                newNotification.type,
                newNotification.referenceId,
                newNotification.text
        );
        return notification;
    }

    public NewNotification createFollowNotificationTemplate(Integer followerId, Integer followingId, String type) {
        NewNotification newNotification = new NewNotification();
        newNotification.senderId = followerId;
        newNotification.type = type;
        newNotification.receiverId = followingId;
        newNotification.referenceId = followerId;
        newNotification.text = "";
        return newNotification;
    }

    public Notification getFollowNotification (Integer followerId, Integer followingId, String type) {
        Notification notification = notificationRepository.findBySenderIdAndReceiverIdAndTypeAndReferenceId(followerId, followingId, type, followerId);
        return notification;
    }

    public ArrayList<NotificationDTO> getUsersNotifications (Integer userId) {

        ArrayList<Notification> userNotifications = notificationRepository.findAllByReceiverId(userId);
        ArrayList<NotificationDTO> userNotificationDTOs = new ArrayList<>();
        if (userNotifications != null) {
            for (Notification notification : userNotifications) {
                    userNotificationDTOs.add(new NotificationDTO(notification));
            }
        }
        return userNotificationDTOs;

    }

    @Transactional
    public void markAllReceiverNotificationsAsSeen(Integer receiverId) {

        notificationRepository.markAllAsSeen(receiverId);

    }

    public boolean checkExistingNotification(NewNotification newNotification) {

        if (notificationRepository.existsBySenderIdAndReceiverIdAndTypeAndReferenceIdAndText(newNotification.senderId, newNotification.receiverId, newNotification.type, newNotification.referenceId, newNotification.text)) {
                return true;
        } else {
            return false;
        }
    }


}
