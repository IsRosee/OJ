package com.yz.oj.controller;

import com.yz.oj.entity.TestResult;
import com.yz.oj.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testResults")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @PostMapping
    public void submitTestResult(@RequestBody TestResult testResult) {
        testResultService.submitTestResult(testResult);
    }

    @GetMapping("/{id}")
    public TestResult getTestResultById(@PathVariable long id) {
        return testResultService.getTestResultById(id);
    }

    @GetMapping("/testPaper/{testPaperId}")
    public List<TestResult> getTestResultsByTestPaperId(@PathVariable long testPaperId) {
        return testResultService.getTestResultsByTestPaperId(testPaperId);
    }

    @GetMapping("/student/{studentId}")
    public List<TestResult> getTestResultsByStudentId(@PathVariable long studentId) {
        return testResultService.getTestResultsByStudentId(studentId);
    }

    @PutMapping("/{id}")
    public void updateTestResult(@PathVariable long id, @RequestBody TestResult testResult) {
        testResult.setId(id);
        testResultService.updateTestResult(testResult);
    }

    @DeleteMapping("/{id}")
    public void deleteTestResult(@PathVariable long id) {
        testResultService.deleteTestResult(id);
    }
}
