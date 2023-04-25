package com.example.Employee_Management_System.dto.response;

import com.example.Employee_Management_System.domain.Priority;
import com.example.Employee_Management_System.domain.Status;
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
public class TaskDetailedInfo implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Status status;
    private Priority priority;
    private int completion;
    private double estimateHours;
    private Long managerId;
    private Long parentId;
    private Long projectId;
    private Long employeeId;
    private String employeeName;
    private String projectName;
    private Integer numberSubtasks;
    private String managerReview;
    private String employeeReview;

    public void update(TaskDetailedInfo other) {
        this.setTitle(other.getTitle());
        this.setStatus(other.getStatus());
        this.setDescription(other.getDescription());
        this.setStartDate(other.getStartDate());
        this.setEndDate(other.getEndDate());
        this.setStatus(other.getStatus());
        this.setPriority(other.getPriority());
        this.setCompletion(other.getCompletion());
        this.setEstimateHours(other.getEstimateHours());
        this.setManagerId(other.getManagerId());
        this.setParentId(other.getParentId());
        this.setProjectId(other.getProjectId());
        this.setEmployeeId(other.getEmployeeId());
        this.setEmployeeName(other.getEmployeeName());
        this.setProjectName(other.getProjectName());
        this.setNumberSubtasks(other.getNumberSubtasks());
        this.setManagerReview(other.getManagerReview());
        this.setEmployeeReview(other.getEmployeeReview());
    }
}
