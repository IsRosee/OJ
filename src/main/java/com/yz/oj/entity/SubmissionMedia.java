package com.yz.oj.entity;

import lombok.Data;
import java.sql.Timestamp;
@Data
public class SubmissionMedia {
    private long id;
    private long submissionId;
    private String mediaType;
    private String mediaUrl;
    private int sortOrder;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
