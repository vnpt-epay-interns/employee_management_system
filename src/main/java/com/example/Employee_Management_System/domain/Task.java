package com.example.Employee_Management_System.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Completion;
import java.sql.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
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
    private Long parentTask;
}
