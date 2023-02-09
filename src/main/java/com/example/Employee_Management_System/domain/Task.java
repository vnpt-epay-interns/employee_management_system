package com.example.Employee_Management_System.domain;

import jakarta.annotation.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Completion;
import java.util.Date;

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
    private String status;
    private Priority priority;
    private Completion completion;
    private double estimateHours;
    private Long employeeId;
}
