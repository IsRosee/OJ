package com.yz.oj.entity;

import lombok.Data;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class ExperimentSubmission {
    private long id;
    private long experimentId;
    private long studentId;
    private String answer;
    private LocalDateTime submittedAt;
    private String status;
}

