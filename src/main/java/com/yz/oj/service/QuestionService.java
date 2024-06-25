package com.yz.oj.service;

import com.yz.oj.mapper.QuestionMapper;
import com.yz.oj.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    public void createQuestion(Question question) {
        questionMapper.insertQuestion(question);
    }

    public Question getQuestionById(long id) {
        return questionMapper.selectQuestionById(id);
    }

    public List<Question> getQuestionsByCreatorId(long creatorId) {
        return questionMapper.selectQuestionsByCreatorId(creatorId);
    }

    public void updateQuestion(Question question) {
        questionMapper.updateQuestion(question);
    }

    public void deleteQuestion(long id) {
        questionMapper.deleteQuestion(id);
    }
}
