package com.example.Employee_Management_System.domain;

import lombok.Data;

import java.sql.Date;
@Data
public class Report {

    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Long employeeId;
    private Long taskId;

}
