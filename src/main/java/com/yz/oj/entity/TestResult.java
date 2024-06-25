package com.yz.oj.entity;
import lombok.Data;
import java.sql.Timestamp;
@Data
public class TestResult {
    private long id;
    private long testPaperId;
    private long studentId;
    private String answers; // JSON format
    private int score;
    private Timestamp completedAt;
}
