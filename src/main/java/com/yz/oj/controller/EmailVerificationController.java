package com.yz.oj.controller;

import com.yz.oj.service.EmailVerificationService;
import com.yz.oj.util.SendEmail;
import com.yz.oj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verify")
public class EmailVerificationController {

    @Autowired
    private UserService UserService;
    @Autowired
    private EmailVerificationService emailVerificationService;
    @Autowired
    private SendEmail sendEmail;

    // 发送验证码
    @PostMapping
    public String verifyEmail(@RequestParam String email) {
        sendEmail.sendEmail(email);
        return "Verification code sent to " + email;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword) {
        boolean isVerified = emailVerificationService.verifyEmail(email, code);
        if (isVerified) {
            boolean isReset = UserService.resetPassword(email, newPassword);
            if (isReset) {
                return "Password reset successful!";
            } else {
                return "Failed to reset password. User not found.";
            }
        } else {
            return "Verification failed. The code is either incorrect or has expired.";
        }
    }
}
