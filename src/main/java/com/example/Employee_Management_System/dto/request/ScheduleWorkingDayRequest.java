package com.example.Employee_Management_System.dto.request;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ScheduleWorkingDayRequest {

    private int year;
    private int month;
    private List<Integer> days;
    private List<String> statuses;
}
