package com.example.Employee_Management_System.dto.request;

import lombok.Data;

@Data
public class UpdateTaskRequest {

    private long taskId;
    private String status;
    private Integer completion;


}
