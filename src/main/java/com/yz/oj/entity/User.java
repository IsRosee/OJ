package com.yz.oj.entity;

import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String salt;
    private String email;
    private UserType userType;

    // 添加用户信息
    private String avatarUrl;
    private String nickname;
    private String signature;
    private String todoList;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
    private Set<Role> roles; // 添加角色列表

    public enum UserType {
        STUDENT,
        TEACHER,
        UNKNOWN
    }

}
