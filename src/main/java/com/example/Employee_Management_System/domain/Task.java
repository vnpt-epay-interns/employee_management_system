package com.example.Employee_Management_System.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//TODO: change the mapper
public class Task implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Status status;
    private Priority priority;
    private int completion;
    private double estimateHours;
    private Long employeeId;
    private Long parentId;
    private Long projectId;
    private String employeeReview;
    private String managerReview;

}
