package com.example.Employee_Management_System.repository;


import com.example.Employee_Management_System.domain.Employee;
import com.example.Employee_Management_System.domain.User;
import com.example.Employee_Management_System.domain.WorkingSchedule;
import com.example.Employee_Management_System.dto.request.ScheduleWorkingDayRequest;
import com.example.Employee_Management_System.dto.response.WorkingScheduleResponse;
import com.example.Employee_Management_System.mapper.EmployeeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class EmployeeRepository {
    private final EmployeeMapper employeeMapper;

    public List<WorkingScheduleResponse> getSchedule(User employee) {
        return employeeMapper.getWorkingSchedule(employee);
    }

    public void scheduleWorkingDays(WorkingSchedule workingSchedule) {
        employeeMapper.scheduleWorkingDays(workingSchedule);
    }


    public void save(Employee employee) {
        employeeMapper.save(employee);
    }
}
