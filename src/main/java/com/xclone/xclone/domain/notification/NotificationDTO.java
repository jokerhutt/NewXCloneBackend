package com.xclone.xclone.domain.notification;

import com.xclone.xclone.domain.post.Post;

import java.sql.Timestamp;
import java.util.ArrayList;

public class NotificationDTO {

    public Integer id;
    public Integer senderId;
    public Integer receiverId;
    public Integer postId;
    public String type;
    public Timestamp createdAt;
    public boolean seen;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.senderId = notification.getSenderId();
        this.receiverId = notification.getReceiverId();
        this.postId = notification.getPostId();
        this.type = notification.getType();
        this.createdAt = notification.getCreatedAt();
        this.seen = notification.isSeen();
    }
}
