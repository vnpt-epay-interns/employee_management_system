package com.example.Employee_Management_System.dto.request;

import lombok.Data;

@Data
public class CreateTaskRequest {

    private String title;
    private String description;
    private String status;
    private Integer completion;
    private Long employeeId;

}
