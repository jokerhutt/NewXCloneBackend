package com.xclone.xclone.domain.notification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/newNotification")
    public ResponseEntity<?> createNotification(@RequestBody NewNotification newNotification) {
        if (notificationService.addNotification(newNotification)) {
            return ResponseEntity.ok("SUCCESS");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("NOTSUCCESS");
        }
    }

    @PostMapping("/markAsSeen")
    public ResponseEntity<?> markAllNotificationsAsSeen(@RequestBody Integer id) {
        notificationService.markAllReceiverNotificationsAsSeen(id);
        return ResponseEntity.ok("SUCCESS");
    }

    @GetMapping("/getAllNotifications/{receiverId}")
    public ResponseEntity<?> getAllNotificationsForUser(@RequestParam Integer receiverId) {

        ArrayList<NotificationDTO> toReturn = notificationService.getUsersNotifications(receiverId);
        return ResponseEntity.ok(toReturn);

    }

}
