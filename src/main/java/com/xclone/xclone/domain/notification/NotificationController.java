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

    @GetMapping("/unseenIds/{id}")
    public ResponseEntity<?> getUsersUnseenNotifications(@PathVariable Integer id) {
        System.out.println("Getting notifications for " + id);
        return ResponseEntity.ok(notificationService.getUsersUnseenIdsAndMarkAllAsSeen(id));
    }

    @PostMapping("/getNotifications")
    public ResponseEntity<?> getNotifications(@RequestBody ArrayList<Integer> ids) {
        System.out.println("Received request to retrieve notifications");
        return ResponseEntity.ok(notificationService.findAllNotificationDTOsById(ids));
    }

}
