package com.yz.oj.entity;

import lombok.Data;
import java.sql.Timestamp;
@Data
public class Question {
    private long id;
    private long creatorId;
    private String questionType;
    private String questionText;
    private String options; // JSON format
    private String answer;  // JSON format
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
