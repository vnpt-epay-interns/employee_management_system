package com.example.Employee_Management_System.dto.request;

import lombok.Data;

import java.sql.Date;

@Data
public class ScheduleWorkingDayRequest {
    private Date date;
    private boolean atMorning;
    private boolean atAfternoon;
}
