package com.yz.oj.service;

import com.yz.oj.mapper.TestPaperMapper;
import com.yz.oj.entity.TestPaper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestPaperService {

    @Autowired
    private TestPaperMapper testPaperMapper;

    public void createTestPaper(TestPaper testPaper) {
        testPaperMapper.insertTestPaper(testPaper);
    }

    public TestPaper getTestPaperById(long id) {
        return testPaperMapper.selectTestPaperById(id);
    }

    public List<TestPaper> getTestPapersByCreatorId(long creatorId) {
        return testPaperMapper.selectTestPapersByCreatorId(creatorId);
    }

    public void updateTestPaper(TestPaper testPaper) {
        testPaperMapper.updateTestPaper(testPaper);
    }

    public void deleteTestPaper(long id) {
        testPaperMapper.deleteTestPaper(id);
    }
}
