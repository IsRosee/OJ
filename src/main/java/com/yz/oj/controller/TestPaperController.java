package com.yz.oj.controller;

import com.yz.oj.entity.TestPaper;
import com.yz.oj.service.TestPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testPapers")
public class TestPaperController {

    @Autowired
    private TestPaperService testPaperService;

    @PostMapping
    public void createTestPaper(@RequestBody TestPaper testPaper) {
        testPaperService.createTestPaper(testPaper);
    }

    @GetMapping("/{id}")
    public TestPaper getTestPaperById(@PathVariable long id) {
        return testPaperService.getTestPaperById(id);
    }

    @GetMapping("/creator/{creatorId}")
    public List<TestPaper> getTestPapersByCreatorId(@PathVariable long creatorId) {
        return testPaperService.getTestPapersByCreatorId(creatorId);
    }

    @PutMapping("/{id}")
    public void updateTestPaper(@PathVariable long id, @RequestBody TestPaper testPaper) {
        testPaper.setId(id);
        testPaperService.updateTestPaper(testPaper);
    }

    @DeleteMapping("/{id}")
    public void deleteTestPaper(@PathVariable long id) {
        testPaperService.deleteTestPaper(id);
    }
}
