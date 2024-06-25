package com.yz.oj.service;

import com.yz.oj.mapper.NotificationMapper;
import com.yz.oj.entity.Notification;
import com.yz.oj.mapper.TeacherAccountMapper;
import com.yz.oj.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private TeacherAccountMapper teacherAccountMapper;

    public void createNotification(Notification notification) {
        notification.setCreatedAt(java.time.LocalDateTime.now());
        notification.setUpdatedAt(java.time.LocalDateTime.now());
        notificationMapper.insertNotification(notification);
    }

    public Notification getNotificationById(long id) {
        return notificationMapper.selectNotificationById(id);
    }

    public List<Notification> getNotificationsByTeacherId(long teacherId) {
        return notificationMapper.selectNotificationsByTeacherId(teacherId);
    }

    public void updateNotification(Notification notification) {
        notificationMapper.updateNotification(notification);
    }

    public void deleteNotification(long id) {
        notificationMapper.deleteNotification(id);
    }

    public Long findTeacherIdByUsername(String username) {
        return teacherAccountMapper.findTeacherIdByUsername(username);
    }

    public List<Notification> findAll() {
        return notificationMapper.selectAll();
    }
}
