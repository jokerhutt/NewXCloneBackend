package com.xclone.xclone.domain.notification;
import com.xclone.xclone.domain.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.ArrayList;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{

    ArrayList<Notification> findAllByReceiverId(Integer receiverId);


    boolean existsBySenderIdAndReceiverIdAndCreatedAt(Integer senderId, Integer receiverId, Timestamp createdAt);

    boolean existsBySenderIdAndReceiverIdAndType(Integer senderId, Integer receiverId, String type);

    @Modifying
    @Query("UPDATE Notification n SET n.seen = true WHERE n.receiverId = :receiverId AND n.seen = false")
    int markAllAsSeen(@Param("receiverId") Integer receiverId);

    boolean existsBySenderIdAndReceiverIdAndTypeAndReferenceIdAndText(Integer senderId, Integer receiverId, String type, Integer referenceId, String text);

    Notification findBySenderIdAndReceiverIdAndTypeAndReferenceIdAndText(Integer senderId, Integer receiverId, String type, Integer referenceId, String text);

    Notification findBySenderIdAndReceiverIdAndTypeAndReferenceId(Integer senderId, Integer receiverId, String type, Integer referenceId);

    boolean existsBySenderIdAndReceiverIdAndTypeAndReferenceId(Integer senderId, Integer receiverId, String type, Integer referenceId);
}
