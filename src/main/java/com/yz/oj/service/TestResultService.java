package com.yz.oj.service;

import com.yz.oj.mapper.TestResultMapper;
import com.yz.oj.entity.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestResultService {

    @Autowired
    private TestResultMapper testResultMapper;

    public void submitTestResult(TestResult testResult) {
        testResultMapper.insertTestResult(testResult);
    }

    public TestResult getTestResultById(long id) {
        return testResultMapper.selectTestResultById(id);
    }

    public List<TestResult> getTestResultsByTestPaperId(long testPaperId) {
        return testResultMapper.selectTestResultsByTestPaperId(testPaperId);
    }

    public List<TestResult> getTestResultsByStudentId(long studentId) {
        return testResultMapper.selectTestResultsByStudentId(studentId);
    }

    public void updateTestResult(TestResult testResult) {
        testResultMapper.updateTestResult(testResult);
    }

    public void deleteTestResult(long id) {
        testResultMapper.deleteTestResult(id);
    }
}
