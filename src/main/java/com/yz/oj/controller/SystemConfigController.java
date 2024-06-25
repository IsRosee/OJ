package com.yz.oj.controller;

import com.yz.oj.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @PostMapping("/student-registration")
    public void setStudentRegistrationStatus(@RequestParam boolean isOpen) {
        systemConfigService.setConfigValue("student_registration_open", Boolean.toString(isOpen));
    }

    @GetMapping("/student-registration")
    public boolean isStudentRegistrationOpen() {
        String value = systemConfigService.getConfigValue("student_registration_open");
        return Boolean.parseBoolean(value);
    }
}
