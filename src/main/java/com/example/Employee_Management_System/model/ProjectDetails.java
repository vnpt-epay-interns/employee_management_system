package com.example.Employee_Management_System.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectDetails {
    private Long id;
    private String name;
    private List<TaskDetailsForProject> tasks;
}
