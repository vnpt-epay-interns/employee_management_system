package com.example.Employee_Management_System.model;

import lombok.Data;

import java.sql.Date;

@Data
public class ProjectBriefInformation {
    private Long id;
    private String name;
    private Date createdAt;
    private Integer employeeNum;
    private Integer taskNum;
    private Object taskNumByStatus;
}
