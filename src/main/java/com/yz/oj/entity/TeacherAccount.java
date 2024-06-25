package com.yz.oj.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeacherAccount {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
