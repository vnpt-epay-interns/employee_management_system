package com.example.Employee_Management_System.dto.request;

import lombok.Data;

import java.sql.Date;

@Data
public class WriteReportRequest {
    private String title;
    private String content;
}
