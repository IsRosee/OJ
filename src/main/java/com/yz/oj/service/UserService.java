package com.yz.oj.service;


import com.yz.oj.controller.UserController;
import com.yz.oj.util.SaltUtil;
import com.yz.oj.entity.*;
import com.yz.oj.mapper.*;
import com.yz.oj.util.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private StudentAccountMapper studentAccountMapper;

    @Autowired
    private TeacherAccountMapper teacherAccountMapper;

    @Autowired
    private PasswordResetTokenMapper passwordResetTokenMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private ExperimentSubmissionService experimentSubmissionService;

    @Autowired  //从容器中自动注入
    private SendEmail sendEmail;

    public User registerStudent(String username, String email, String password, String studentNumber) {
//        注册限定哦
//        if (!isStudentRegistrationOpen()) {
//            throw new RuntimeException("Student registration is currently closed.");
//        }
        String salt = SaltUtil.generateSalt();
        String hashedPassword = passwordEncoder.encode(password + salt);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);
        user.setSalt(salt);
        user.setUserType(User.UserType.STUDENT);
        user.setActive(true);
        // set LocalDateTime createdAt and updatedAt
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insertUser(user);

        Role studentRole = roleMapper.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_STUDENT"));
        roleMapper.insertUserRole(user.getId(), studentRole.getId());


        StudentAccount studentAccount = new StudentAccount();
        studentAccount.setUserId(user.getId());
        studentAccount.setStudentNumber(studentNumber);
        studentAccount.setCreatedAt(LocalDateTime.now());
        studentAccount.setUpdatedAt(LocalDateTime.now());
        studentAccountMapper.insertStudentAccount(studentAccount);

        return user;
    }

    public User registerTeacher(String username, String email, String password) {
        String salt = SaltUtil.generateSalt(); // 生成盐
        String hashedPassword = passwordEncoder.encode(password + salt);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hashedPassword);
        user.setSalt(salt);
        user.setUserType(User.UserType.TEACHER);
        user.setActive(true);
        // set LocalDateTime createdAt and updatedAt
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insertUser(user);

        Role teacherRole = roleMapper.findByName("ROLE_TEACHER")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_TEACHER"));
        roleMapper.insertUserRole(user.getId(), teacherRole.getId());

        TeacherAccount teacherAccount = new TeacherAccount();
        teacherAccount.setUserId(user.getId());
        teacherAccount.setCreatedAt(LocalDateTime.now());
        teacherAccount.setUpdatedAt(LocalDateTime.now());
        teacherAccountMapper.insertTeacherAccount(teacherAccount);

        return user;
    }

    public Optional<User> findByUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            return Optional.empty();
        }
        return findByUsername(userDetails.getUsername());
    }
    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUserId(user.getId());
        myToken.setToken(token);
        passwordResetTokenMapper.insertPasswordResetToken(myToken);
    }

    public boolean isStudentRegistrationOpen() {
        String value = systemConfigService.getConfigValue("student_registration_open");
        if (value == null) {
            return true; // default value
        }
        return Boolean.parseBoolean(value);
    }

    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public StudentAccount getStudentAccountByUserId(Long id) {
        return studentAccountMapper.findByUserId(id).orElseThrow(() -> new RuntimeException("Student account not found"));
    }

    public List<User> getStudents() {
        return userMapper.findByUserType(User.UserType.STUDENT);
    }

    public User updateUser(Long id, UserController.UserRequest userResponse) {
        User user = (User) userMapper.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userResponse.getUsername());
        user.setEmail(userResponse.getEmail());
        user.setAvatarUrl(userResponse.getAvatarUrl());
        user.setNickname(userResponse.getNickname());
        user.setSignature(userResponse.getSignature());
        user.setTodoList(userResponse.getTodoList());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateUser(user);
        return user;
    }

    public String uploadAvatar(MultipartFile file) throws IOException {
        // 返回的是file/开头的可获取URL，不是在后端服务器上的文件路径
        return fileStorageService.storeFile(file);
    }

    public void updateStudentAccount(StudentAccount studentAccount) {
        studentAccount.setUpdatedAt(LocalDateTime.now());
        studentAccountMapper.updateStudentAccount(studentAccount);
    }

    public Optional<User> getUserById(Long userId) {
        return userMapper.findById(userId);
    }

    public void deleteUser(Long id) {
        //删除学生账号、用户角色、实验提交文件、实验提交、用户账号
        studentAccountMapper.deleteStudentAccountByUserId(id);
        roleMapper.deleteUserRoles(id);
        // 删除提交的媒体文件和提交
        experimentSubmissionService.deleteSubmission(id);
        // 删除用户
        userMapper.deleteUser(id);
    }

    public boolean resetPassword(String email, String newPassword) {
        User user = userMapper.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String salt = SaltUtil.generateSalt();
        String hashedPassword = passwordEncoder.encode(newPassword + salt);
        user.setPasswordHash(hashedPassword);
        user.setSalt(salt);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateUser(user);
        return true;
    }

    public void disableUser(Long id) {
        User user = userMapper.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    public void enableUser(Long id) {
        User user = userMapper.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateUser(user);
    }

    // 重置为默认密码：学号+a
    public void resetDefaultPassword(Long id) {
        User user = userMapper.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        String salt = SaltUtil.generateSalt();
        String hashedPassword = passwordEncoder.encode(getStudentAccountByUserId(id).getStudentNumber() + "a" + salt);
        user.setPasswordHash(hashedPassword);
        user.setSalt(salt);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateUser(user);
    }
}

