package com.yz.oj.entity;

import lombok.Data;
import java.sql.Timestamp;
@Data
public class TestPaper {
    private long id;
    private long creatorId;
    private String title;
    private String description;
    private String questions; // JSON format
    private String settings;  // JSON format
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
