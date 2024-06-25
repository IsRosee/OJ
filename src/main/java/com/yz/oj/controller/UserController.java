package com.yz.oj.controller;

import com.yz.oj.entity.StudentAccount;
import com.yz.oj.entity.User;
import com.yz.oj.service.CustomUserDetailsService;
import com.yz.oj.service.UserService;
import com.yz.oj.util.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final String BEARER_PREFIX = "Bearer ";
    @Autowired
    private UserService userService;

    @Autowired
//    @Lazy
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtService jwtService;

//    获取当前用户信息，包括学生特殊信息（学号），详见UserResponse
    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        var jwt = authorization.substring(BEARER_PREFIX.length());
        try {
            Map<String, Object> claims = jwtService.parseToken(jwt);
            Long userId = Long.parseLong(claims.get("userId").toString());
            Optional<User> user = userService.getUserById(userId);
            if (user.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.get().getId());
            userResponse.setUsername(user.get().getUsername());
            userResponse.setEmail(user.get().getEmail());
            userResponse.setUserType(user.get().getUserType());
            userResponse.setCreatedAt(user.get().getCreatedAt());
            userResponse.setUpdatedAt(user.get().getUpdatedAt());
            userResponse.setActive(user.get().isActive());
            userResponse.setAvatarUrl(user.get().getAvatarUrl());
            userResponse.setNickname(user.get().getNickname());
            userResponse.setSignature(user.get().getSignature());
            userResponse.setTodoList(user.get().getTodoList());
            if (user.get().getUserType() == User.UserType.STUDENT) {
                StudentAccount studentAccount = userService.getStudentAccountByUserId(user.get().getId());
                userResponse.setStudentNumber(studentAccount.getStudentNumber());
            }
            return ResponseEntity.ok(userResponse);
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    // 返回所有学生信息,包括下面UserResponse中的所有信息
    @GetMapping("/students")
    public ResponseEntity<?> getStudents() {
        List<User> students = userService.getStudents();
//        System.out.println(students);
        return ResponseEntity.ok(students.stream().map(user -> {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setEmail(user.getEmail());
            userResponse.setUserType(user.getUserType());
            userResponse.setCreatedAt(user.getCreatedAt());
            userResponse.setUpdatedAt(user.getUpdatedAt());
            userResponse.setActive(user.isActive());
            userResponse.setAvatarUrl(user.getAvatarUrl());
            userResponse.setNickname(user.getNickname());
            userResponse.setSignature(user.getSignature());
            userResponse.setTodoList(user.getTodoList());
            if (user.getUserType() == User.UserType.STUDENT) {
                StudentAccount studentAccount = userService.getStudentAccountByUserId(user.getId());
                userResponse.setStudentNumber(studentAccount.getStudentNumber());
            }
            return userResponse;
        }).collect(Collectors.toList()));
    }

    // 上传头像文件，返回存储头像的url（与将来数据库中的avatarUrl字段相同）
    @PostMapping("/upload")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        String avatarUrl = userService.uploadAvatar(file);
        return ResponseEntity.ok(avatarUrl);
    }

    // 更新某个用户信息（先上传头像，再更新用户信息）
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        User user = userService.updateUser(id, userRequest);
        // 如果是学生，更新学生特殊信息
        if (user.getUserType() == User.UserType.STUDENT) {
            StudentAccount studentAccount = userService.getStudentAccountByUserId(user.getId());
            studentAccount.setStudentNumber(userRequest.getStudentNumber());
            userService.updateStudentAccount(studentAccount);
        }
        return ResponseEntity.ok(user);
    }

    // 删除学生
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    // 禁用学生
    @PutMapping("/user/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.ok().build();
    }
    // 启用学生
    @PutMapping("/user/{id}/enable")
    public ResponseEntity<?> enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return ResponseEntity.ok().build();
    }

// 重置学生默认密码
    @PutMapping("/user/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id) {
        userService.resetDefaultPassword(id);
        return ResponseEntity.ok().build();
    }




    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterRequest request) {
        //        注册限定哦
        if (!userService.isStudentRegistrationOpen()) {
            // 状态码409
            return ResponseEntity.status(409).body("学生注册已关闭");
        }
        User user = userService.registerStudent(request.getUsername(), request.getEmail(), request.getPassword(), request.getStudentNumber());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody RegisterRequest request) {
        User user = userService.registerTeacher(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println(request.getUsername());
        System.out.println(request.getPassword());
        Authentication token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            return ResponseEntity.ok("Login successful");
//            返回jwt  String createLoginAccessToken(UserDetails user)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 如果当前用户已被禁用
            if (!userDetails.isEnabled()) {
                return ResponseEntity.badRequest().body("用户已被禁用");
            }
            String jwt = jwtService.createLoginAccessToken(userDetails);
            return ResponseEntity.ok(jwt);
        } catch (UsernameNotFoundException | AccountStatusException e) {
            return ResponseEntity.badRequest().body("用户不存在");
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("密码错误");
        }
    }

    @GetMapping("/who-am-i")
    public Map<String, Object> whoAmI(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        var jwt = authorization.substring(BEARER_PREFIX.length());
        try {
            return jwtService.parseToken(jwt);
        } catch (JwtException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    @PostMapping("/password-reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        // Implement password reset logic
        return ResponseEntity.ok("Password reset email sent");
    }

    // 请求体放在这里
    @Data
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String studentNumber; // Optional for student registration

        // Getters and setters
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;

    }
    @Data
    public static class PasswordResetRequest {
        private String email;

    }

    @Data
    public static class UserResponse {
        private Long id;
        private String username;
        private String email;
        private User.UserType userType;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private boolean isActive;
        // 学生特殊信息
        private String studentNumber;
        // 添加用户信息
        private String avatarUrl;
        private String nickname;
        private String signature;
        private String todoList;
    }

    @Data
    public static class UserRequest {
        private String username;
        private String email;
        private String studentNumber;
        private String avatarUrl;
        private String nickname;
        private String signature;
        private String todoList;
    }

}

