package com.yz.oj.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasswordResetToken {
    private Long id;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
}
