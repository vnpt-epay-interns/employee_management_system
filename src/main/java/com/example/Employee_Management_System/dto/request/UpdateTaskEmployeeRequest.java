package com.example.Employee_Management_System.dto.request;

import com.example.Employee_Management_System.domain.Status;
import lombok.Data;

@Data
public class UpdateTaskEmployeeRequest {
    private Status status;
    private int completion;
    private String employeeReview;
}
