package com.yz.oj.entity;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class SystemConfig {
    private long id;
    private String key;
    private String value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
