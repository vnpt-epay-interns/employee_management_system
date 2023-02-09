package com.example.Employee_Management_System.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkingSchedule {
    private Long id;
    private Long employeeId;
    private String date;
    private boolean atMorning;
    private boolean atAfternoon;
}
