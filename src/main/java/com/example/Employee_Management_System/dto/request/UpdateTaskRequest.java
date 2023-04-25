package com.example.Employee_Management_System.dto.request;

import com.example.Employee_Management_System.domain.Priority;
import com.example.Employee_Management_System.domain.Status;
import lombok.Data;

import java.sql.Date;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private Status status;
    private Integer completion;
    private Priority priority;
    private Date startDate;
    private String review;
    private Date endDate;
    private Long employeeId;
    private double estimateHours;
    private Long parentId;
    private Long projectId;
    private String employeeReview;
    private String managerReview;
}
