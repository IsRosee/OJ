package com.yz.oj.controller;

import com.yz.oj.entity.Notification;
import com.yz.oj.service.NotificationService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody NotificationRequest notification) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not authenticated");
        }

        // 也可以上面得到当前登录用户的Role
        String username = authentication.getName();
        Long teacherId = notificationService.findTeacherIdByUsername(username);
        if (teacherId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid teacher ID");
        }

        notificationService.createNotification(new Notification(teacherId, notification.getTitle(), notification.getContent()));
        Map<String, String> response = new HashMap<>();
        response.put("title", notification.getTitle());
        return ResponseEntity.ok("Notification created successfully");
    }

    // Request method 'GET' is not supported是因为注意前缀是/api/notifications
    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = notificationService.findAll();
        return ResponseEntity.ok(notifications);
    }
//    @GetMapping("/{id}")
//    public Notification getNotificationById(@PathVariable long id) {
//        return notificationService.getNotificationById(id);
//    }

    @GetMapping("/teacher/{teacherId}")
    public List<Notification> getNotificationsByTeacherId(@PathVariable long teacherId) {
        return notificationService.getNotificationsByTeacherId(teacherId);
    }

    @PutMapping("/{id}")
    public void updateNotification(@PathVariable long id, @RequestBody Notification notification) {
        notification.setId(id);
        notificationService.updateNotification(notification);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable long id) {
        notificationService.deleteNotification(id);
    }

    @Data
    public static class NotificationRequest {
        private String title;
        private String content;
    }
}
