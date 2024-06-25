package com.yz.oj.controller;

import com.yz.oj.entity.Question;
import com.yz.oj.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    public void createQuestion(@RequestBody Question question) {
        questionService.createQuestion(question);
    }

    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable long id) {
        return questionService.getQuestionById(id);
    }

    @GetMapping("/creator/{creatorId}")
    public List<Question> getQuestionsByCreatorId(@PathVariable long creatorId) {
        return questionService.getQuestionsByCreatorId(creatorId);
    }

    @PutMapping("/{id}")
    public void updateQuestion(@PathVariable long id, @RequestBody Question question) {
        question.setId(id);
        questionService.updateQuestion(question);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable long id) {
        questionService.deleteQuestion(id);
    }
}
