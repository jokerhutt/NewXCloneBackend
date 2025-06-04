package com.xclone.xclone.domain.notification;

import com.xclone.xclone.domain.post.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

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

    public boolean markAllAsSeen(ArrayList<Integer> notificationIds) {

        for (int i = 0; i < notificationIds.size(); i++) {
            markAsSeen(notificationIds.get(i));
        }

        return true;

    }

    @Transactional
    public boolean markAsSeen (Integer notificationId) {

        Optional<Notification> notification = notificationRepository.findById(notificationId);

            if (!notification.isPresent()) return false;
            if (notification.get().isSeen()) return false;

            notification.get().setSeen(true);
            notificationRepository.save(notification.get());

            return true;
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
