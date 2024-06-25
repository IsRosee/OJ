package com.yz.oj.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExperimentMedia {
    private long id;
    private long experimentId;
    private String mediaType;
    private String mediaUrl;
    private int sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
