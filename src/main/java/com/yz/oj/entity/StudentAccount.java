package com.yz.oj.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentAccount {
    private Long id;
    private Long userId;
    private String studentNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
