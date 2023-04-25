package com.example.Employee_Management_System.dto.request;

import com.example.Employee_Management_System.domain.Priority;
import com.example.Employee_Management_System.domain.Status;
import lombok.Data;

import java.sql.Date;

@Data
public class CreateTaskRequest {
    private String title;
    private String description;
    private Status status;
    private Integer completion;
    private Priority priority;
    private String review;
    private Date startDate;
    private Date endDate;
    private Long employeeId;
    private Double estimateHours;
    private Long parentId;
    private Long projectId;
}
