package com.example.Employee_Management_System.model;

import com.example.Employee_Management_System.domain.Report;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class ReportDetailedInfo {
    private Long id;
    private String title;
    private String content;
    private Long taskId;
    private String employeeName;
    private Date createdAt;
    private boolean isRead;

}
