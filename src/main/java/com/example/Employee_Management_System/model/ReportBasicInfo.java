package com.example.Employee_Management_System.model;

import com.example.Employee_Management_System.domain.Report;
import lombok.Data;

import java.sql.Date;

@Data
public class ReportBasicInfo {
    private long reportId;
    private String reportTitle;
    private String employeeName;
    private Date createdAt;
}
