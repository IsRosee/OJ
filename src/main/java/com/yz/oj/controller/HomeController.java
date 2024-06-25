package com.yz.oj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 新的网页记得在这里添加路由
    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/testme")
    public String testme() {
        return "testme";
    }

    @GetMapping("/experiment")
    public String experiment() {
        return "experiment";
    }

    @GetMapping("/experimentDetail/{id}")
    public String experimentDetail() {
        return "experimentDetail";
    }
    @GetMapping("/submissionDetail/{id}")
    public String submissionDetail() {
        return "submissionDetail";
    }

    @GetMapping("/me")
    public String me() {
        return "me";
    }

    @GetMapping("/notification")
    public String notification() {
        return "notification";
    }


}
