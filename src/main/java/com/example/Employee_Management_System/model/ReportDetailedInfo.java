package com.example.Employee_Management_System.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class ReportDetailedInfo {
    private long id;
    private String title;
    private String content;
    private String employeeName;
    private Date createdAt;
    private Long taskId;
    private boolean isRead;
}
