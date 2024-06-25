package com.yz.oj.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Notification {
    private long id;
    private long teacherId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Notification(Long teacherId, String title, String content) {
        this.teacherId = teacherId;
        this.title = title;
        this.content = content;
    }
}
