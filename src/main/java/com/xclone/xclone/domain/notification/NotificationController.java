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

    @GetMapping("/unseenIds/{id}")
    public ResponseEntity<?> getUsersInseenNotifications(@PathVariable Integer id) {
        System.out.println("Getting notifications for " + id);

        return ResponseEntity.ok(notificationService.getUsersUnseenIds(id));
    }

    @GetMapping("/getAllNotifications/{receiverId}")
    public ResponseEntity<?> getAllNotificationsForUser(@PathVariable Integer receiverId) {
        ArrayList<NotificationDTO> toReturn = notificationService.getUsersNotifications(receiverId);
        System.out.println(toReturn.size());
        return ResponseEntity.ok(toReturn);
    }

    @PostMapping("/getNotifications")
    public ResponseEntity<?> getNotifications(@RequestBody ArrayList<Integer> ids) {
        System.out.println("Received request to retrieve notifications");
        return ResponseEntity.ok(notificationService.findAllNotificationDTOsById(ids));
    }

}
