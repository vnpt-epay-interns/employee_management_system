package com.example.Employee_Management_System.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;
@Data
@Builder
@AllArgsConstructor
public class Report {

    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Long createdBy;
    private Long taskId;
    private boolean isRead;

}
