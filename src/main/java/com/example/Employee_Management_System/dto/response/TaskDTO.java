package com.example.Employee_Management_System.dto.response;

import com.example.Employee_Management_System.domain.Priority;
import com.example.Employee_Management_System.domain.Status;
import com.example.Employee_Management_System.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Status status;
    private Priority priority;
    private int completion;
    private double estimateHours;
    private Long parentId;
    private Long projectId;
    private Long employeeId;
    private String employeeName;
    private Integer numberReports;
}
