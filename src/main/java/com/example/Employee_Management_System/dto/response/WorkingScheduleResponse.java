package com.example.Employee_Management_System.dto.response;

import lombok.Data;

import java.sql.Date;

@Data
public class WorkingScheduleResponse {

    private Long employeeId;

    private Date date;
    private String employeeName;
    private boolean atMorning;
    private boolean atAfternoon;
}
