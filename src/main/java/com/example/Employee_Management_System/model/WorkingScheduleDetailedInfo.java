package com.example.Employee_Management_System.model;

import lombok.Data;

//TODO: change the model to the domain?
@Data
public class WorkingScheduleDetailedInfo {
    private Long employeeId;
    private String employeeName;
    private int day;
    private String status;
}
