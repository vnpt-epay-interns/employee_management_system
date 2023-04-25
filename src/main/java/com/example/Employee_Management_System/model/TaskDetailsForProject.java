package com.example.Employee_Management_System.model;

import lombok.Data;

import java.sql.Date;

@Data
public class TaskDetailsForProject {
    private Long id;
    private String title;
    private String description;
    private String completion;
    private Date startDate;
    private Date endDate;
    private Integer estimateHours;
    private String assigneeName;
    private String managerReview;
    private String employeeReview;
}
